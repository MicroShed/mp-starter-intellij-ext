/*
 *  Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 *  See the NOTICE file(s) distributed with this work for additional
 *  information regarding copyright ownership.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.microshed.intellij;


import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.download.DownloadableFileDescription;
import com.intellij.util.download.DownloadableFileService;
import com.intellij.util.download.FileDownloader;
import com.intellij.util.io.Decompressor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;
import org.microshed.intellij.model.ModuleInitializationData;
import org.microshed.intellij.wizard.MicroProfileSelectionStep;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The build is extending from Java to ensure it will be like any normal Java module. However it uses the {@link MicroProfileCompatibleJavaModuleType}
 * to ensure it can limit the possible JDKs to JDK 8. This can be removed if later we add support for JDK 11 to MicroProfile Starter.
 *
 * @author Ehsan Zaery Moghaddam (zaerymoghaddam@gmail.com)
 */
public class MicroProfileModuleBuilder extends JavaModuleBuilder {

    public static final String STARTER_REST_BASE_URL = "https://start.microprofile.io";
    private static final Logger LOG = Logger.getInstance("#org.microprofile.starter.intellij.MicroProfileModuleBuilder");
    private final ModuleInitializationData moduleCreationData = new ModuleInitializationData();

    @Override
    public ModuleType getModuleType() {
        return MicroProfileCompatibleJavaModuleType.JAVA;
    }

    @Override
    public String getParentGroup() {
        return "MicroProfile Starter";
    }

    @Override
    public Icon getNodeIcon() {
        return MicroProfilePluginIcons.MICROPROFILE_ICON;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @Override
    public String getDescription() {
        return "A MicroProfile module is used to start developing microservices using MicroProfile. It uses MicroProfile Starter to setup proper " +
                "dependencies and configurations based on the MicroProfile runtime that you choose.";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getPresentableName() {
        return "MicroProfile Starter";
    }

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new MicroProfileSelectionStep(moduleCreationData, context);
    }

    /**
     * By overwriting this method, we ensure that the artifactId chosen by the user would be used as the default value for the folder name.
     *
     * @param settingsStep step to be modified
     * @return callback ({@link ModuleWizardStep#validate()} and {@link ModuleWizardStep#updateDataModel()} will be invoked)
     */
    @Nullable
    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        ModuleNameLocationSettings moduleNameLocationSettings = settingsStep.getModuleNameLocationSettings();
        if (moduleNameLocationSettings != null && moduleCreationData.getArtifactId() != null) {
            moduleNameLocationSettings.setModuleName(StringUtil.sanitizeJavaIdentifier(moduleCreationData.getArtifactId()));
        }

        return super.modifySettingsStep(settingsStep);
    }

    /**
     * Calls MicroProfile Starter REST API and fetches the initial project structure, extract it to a temp folder, copy that to the module root and
     * ensures that all pom.xml files found in the folder are detected by IntelliJ as Maven module.
     */
    @Override
    public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        super.setupRootModel(modifiableRootModel);
        final Project project = modifiableRootModel.getProject();

        final VirtualFile root = createAndGetContentEntry();
        if (root == null) {
            Messages.showErrorDialog("Unable to configure the project module. Module content root path is null.", "");
            return;
        }

        File tempFolder = new File(PathManager.getTempPath());
        try {

            VirtualFile virtualFile = downloadStarterProjectZip(project, tempFolder);
            if (virtualFile == null) {
                Messages.showErrorDialog(
                        "Unable to download the initial MicroProfile project setup from " + STARTER_REST_BASE_URL +
                                ". Check your network connection.", "");
                return;
            }

            File tempExtractDirectory = extractProjectZip(tempFolder, virtualFile);
            if (tempExtractDirectory == null) {
                Messages.showErrorDialog("Unable to extract the initial MicroProfile project setup into " + tempFolder, "");
                return;
            }

            FileUtil.copyDirContent(
                    new File(tempExtractDirectory.getAbsolutePath() + File.separatorChar + moduleCreationData.getArtifactId()),
                    new File(root.getPresentableUrl()));


            List<VirtualFile> pomFilesVfs = getPomVirtualFiles(root);
            configurePomFilesAsMavenModule(project, pomFilesVfs);
        } catch (IOException e) {
            LOG.warn(e);
        }
    }

    private void configurePomFilesAsMavenModule(Project project, List<VirtualFile> pomFilesVfs) {
        DataContext projectContext = SimpleDataContext.getProjectContext(project);
        MavenProjectsManager manager = MavenActionUtil.getProjectsManager(projectContext);
        if (manager != null) {
            manager.addManagedFilesOrUnignore(pomFilesVfs);
        }
    }

    @NotNull
    private List<VirtualFile> getPomVirtualFiles(VirtualFile root) {
        List<File> pomFiles = FileUtil.findFilesByMask(Pattern.compile("pom\\.xml"), new File(root.getPresentableUrl()));
        return pomFiles.stream().map(pf -> LocalFileSystem.getInstance().findFileByPath(pf.getPath())).collect(Collectors.toList());
    }

    private File extractProjectZip(File tempFolder, VirtualFile virtualFile) {
        Decompressor.Zip zipFile = new Decompressor.Zip(new File(virtualFile.getPresentableUrl()));
        try {
            File tempExtractDirectory = FileUtil.createTempDirectory(tempFolder, "microprofile_starter_",
                    new Random(new Date().getTime()).nextInt() + "_extract", true);
            zipFile.extract(tempExtractDirectory);
            return tempExtractDirectory;
        } catch (IOException e) {
            LOG.warn(e);
        }

        return null;
    }

    private VirtualFile downloadStarterProjectZip(Project project, File tempFolder) throws IOException {
        String url = STARTER_REST_BASE_URL + "/api/2/project?" + moduleCreationData.toQueryString();
        File file = FileUtil.createTempFile(tempFolder, "microprofile_starter_", "_download", true, true);

        final DownloadableFileService downloadService = DownloadableFileService.getInstance();
        DownloadableFileDescription fileDescription = downloadService.createFileDescription(url, moduleCreationData.getArtifactId() +
                new Date().getTime() + ".zip");
        FileDownloader downloader =
                downloadService.createDownloader(Collections.singletonList(fileDescription), fileDescription.getPresentableFileName());

        List<VirtualFile> virtualFiles = downloader.downloadFilesWithProgress(file.getParent(), project, null);
        if (virtualFiles != null && !virtualFiles.isEmpty()) {
            return virtualFiles.get(0);
        }

        return null;
    }

    private VirtualFile createAndGetContentEntry() {
        String contentEntryPath = getContentEntryPath();
        if (contentEntryPath == null) {
            LOG.warn("Unable to configure the project module. Module content root path is null.");
            return null;
        }
        String path = FileUtil.toSystemIndependentName(contentEntryPath);
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }
}

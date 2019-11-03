/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.microshed.intellij;

import com.intellij.ide.util.projectWizard.JavaSettingsStep;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.lang.JavaVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ehsan Zaery Moghaddam (ezm@one.com)
 */
public class MicroProfileCompatibleJavaModuleType extends JavaModuleType {

    public static final ModuleType JAVA;

    static {
        try {
            JAVA = (ModuleType) Class.forName("org.microshed.intellij." +
                    "MicroProfileCompatibleJavaModuleType").newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Nullable
    @Override
    public ModuleWizardStep modifyProjectTypeStep(
            @NotNull SettingsStep settingsStep,
            @NotNull ModuleBuilder moduleBuilder) {
        return new JavaSettingsStep(settingsStep, moduleBuilder, moduleBuilder::isSuitableSdkType) {
            @Override
            public boolean validate() throws ConfigurationException {
                boolean result = super.validate();

                if (result) {
                    Sdk jdk = myJdkComboBox.getSelectedJdk();
                    if (jdk != null && jdk.getVersionString() != null) {
                        JavaVersion javaVersion = JavaVersion.parse(jdk.getVersionString());
                        if (javaVersion.feature != 8) {
                            Messages.showErrorDialog("MicroProfile Starter requires JDK 8", "");
                            result = false;
                        }
                    } else {
                        Messages.showErrorDialog("MicroProfile Starter requires JDK 8", "");
                        result = false;
                    }
                }

                return result;
            }
        };
    }
}

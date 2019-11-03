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
package org.microshed.intellij.model;

import com.intellij.openapi.projectRoots.Sdk;

import java.util.List;

/**
 * Container class for parameters entered in the new MicroProfile module wizard. It would be used in the last step to generate the module.
 *
 * @author Ehsan Zaery Moghaddam (zaerymoghaddam@gmail.com)
 */
public class ModuleInitializationData {
    private String groupId;
    private String artifactId;
    private String mpVersion;
    private String mpServer;
    private List<String> mpSpecs;

    private Sdk jdk;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public Sdk getJdk() {
        return jdk;
    }

    public void setJdk(Sdk jdk) {
        this.jdk = jdk;
    }

    public String getMpVersion() {
        return mpVersion;
    }

    public void setMpVersion(String mpVersion) {
        this.mpVersion = mpVersion;
    }

    public String getMpServer() {
        return mpServer;
    }

    public void setMpServer(String mpServer) {
        this.mpServer = mpServer;
    }

    public List<String> getMpSpecs() {
        return mpSpecs;
    }

    public void setMpSpecs(List<String> mpSpecs) {
        this.mpSpecs = mpSpecs;
    }

    /**
     * Converts the details gathered through the wizard to a query string format. It can be used when requesting starter rest api to generate the
     * project.
     *
     * @return The module details entered by the user in query string format
     */
    public String toQueryString() {
        StringBuilder query = new StringBuilder();

        query.append("artifactId=").append(artifactId);
        query.append("&groupId=").append(groupId);
        query.append("&mpVersion=").append(mpVersion);
        query.append("&supportedServer=").append(mpServer);

        for (String spec : mpSpecs) {
            query.append("&selectedSpecs=").append(spec);
        }

        return query.toString();
    }
}

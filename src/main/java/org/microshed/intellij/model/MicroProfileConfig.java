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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ehsan Zaery Moghaddam (zaerymoghaddam@gmail.com)
 */
public class MicroProfileConfig {
    private List<String> supportedServers = new ArrayList<>();
    private List<String> specs = new ArrayList<>();
    private List<String> specCodes = new ArrayList<>();

    public List<String> getSpecCodes() {
        return specCodes;
    }

    public void setSpecCodes(List<String> specCodes) {
        this.specCodes = specCodes;
    }

    public List<String> getSupportedServers() {
        return supportedServers;
    }

    public void setSupportedServers(List<String> supportedServers) {
        this.supportedServers = supportedServers;
    }

    public List<String> getSpecs() {
        return specs;
    }

    public void setSpecs(List<String> specs) {
        this.specs = specs;
    }
}

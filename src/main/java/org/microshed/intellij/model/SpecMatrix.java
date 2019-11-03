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
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Ehsan Zaery Moghaddam (zaerymoghaddam@gmail.com)
 */
public class SpecMatrix {
    private Map<String, MicroProfileConfig> configs = new TreeMap<>();
    private Map<String, String> descriptions = new TreeMap<>();

    public Map<String, MicroProfileConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, MicroProfileConfig> configs) {
        this.configs = configs;
    }

    public Map<String, String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<String, String> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Each description begins with the name of the spec, followed by a "-" and then the actual description. This method converts the internal list of
     * specs to a map having spec name as a key and the actual description as value
     *
     * @return Map of human readable spec names to their corresponding description
     */
    public List<MicroProfileSpec> getParsedDescription() {
        List<MicroProfileSpec> result = new ArrayList<>(descriptions.size());

        descriptions.forEach((key, value) -> {
            String[] specDetails = value.split(" - ");
            result.add(new MicroProfileSpec(key, specDetails[0], specDetails[1]));
        });

        return result;
    }

}

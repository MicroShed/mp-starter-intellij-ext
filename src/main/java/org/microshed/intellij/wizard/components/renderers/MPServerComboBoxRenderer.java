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
package org.microshed.intellij.wizard.components.renderers;

import com.intellij.ui.ColoredListCellRenderer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

/**
 * Given the MicroProfile server name as returned by MicroProfiler Starter REST
 * API (e.g. THORNTAIL_V2 or PAYARA_MICRO) and renders them as in a human
 * readable form (e.g. Thorntail V2 or Payara Micro)
 *
 * @author Ehsan Zaery Moghaddam (zaerymoghaddam@gmail.com)
 */
public class MPServerComboBoxRenderer extends ColoredListCellRenderer<String> {

    @Override
    protected void customizeCellRenderer(@NotNull JList<? extends String> list,
                                         String mpVersion, int index,
                                         boolean selected, boolean hasFocus) {
        String[] serverNameParts = mpVersion.split("_");
        String serverName = Arrays.stream(serverNameParts)
                .map(String::toLowerCase)
                .map(StringUtils::capitalize)
                .reduce("", (str1, str2) -> str1 + " " + str2);

        append(serverName);
    }
}

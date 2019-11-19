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
package org.microshed.intellij.wizard.components;

import com.intellij.ui.*;
import com.intellij.util.ui.tree.TreeUtil;
import org.microshed.intellij.MicroProfilePluginIcons;
import org.microshed.intellij.model.MicroProfileSpec;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.List;

/**
 * The check box list of specs for a MicroProfile version. User is allowed to choose specs that he/she is interested to receive examples for.
 *
 * @author Ehsan Zaery Moghaddam (zaerymoghaddam@gmail.com)
 */
public class MPSpecCheckboxTree extends CheckboxTree {

    public MPSpecCheckboxTree(List<MicroProfileSpec> specs) {
        super(new MPSpecsCheckboxTreeCellRenderer(), new CheckedTreeNode());
        setShowsRootHandles(false);
        setRowHeight(0);
        setRootVisible(false);
        setBorder(new SideBorder(JBColor.border(), SideBorder.ALL));
        setModel(specs);
    }

    public void setModel(List<MicroProfileSpec> specs) {
        CheckedTreeNode root = new CheckedTreeNode();
        List<CheckedTreeNode> specsNodes = new ArrayList<>();
        specs.forEach(spec -> {
            //  Ensure all specs are unselected by default
            CheckedTreeNode specCheckNode = new CheckedTreeNode(spec);
            specCheckNode.setChecked(false);
            specsNodes.add(specCheckNode);
        });
        TreeUtil.addChildrenTo(root, specsNodes);
        setModel(new DefaultTreeModel(root));
    }

    static class MPSpecsCheckboxTreeCellRenderer extends CheckboxTree.CheckboxTreeCellRenderer {

        @Override
        public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            ColoredTreeCellRenderer renderer = getTextRenderer();
            CheckedTreeNode treeNode = (CheckedTreeNode) value;

            if (treeNode.getUserObject() != null) {  //   root node doesn't have any model object assigned and is not rendered
                renderer.append(((MicroProfileSpec) treeNode.getUserObject()).getTitle());
                renderer.setIcon(MicroProfilePluginIcons.MICROPROFILE_ICON);
            }
        }
    }

}

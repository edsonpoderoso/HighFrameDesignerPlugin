/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@TemplateRegistration(
        folder = "Other",
        content = {"HFD.hfd","HFD.hfdcat"},
        iconBase="org/edsonmoreira/editor/HFD.png",
        displayName="#HFDtemplate_displayName",
        description="Description.html",
        scriptEngine="freemarker")
@Messages(value = "HFDtemplate_displayName=Empty HFD file")
package org.edsonmoreira.editor;

import org.netbeans.api.templates.TemplateRegistration;
import  org.openide.util.NbBundle.Messages;

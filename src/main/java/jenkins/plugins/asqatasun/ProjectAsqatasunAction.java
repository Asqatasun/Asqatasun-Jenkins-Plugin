/*
 * Asqatasun Runner - Trigger Automated webpage assessment from Jenkins 
 * Copyright (C) 2015 Asqatasun.org
 *
 * This file is part of Asqatasun.
 *
 * Asqatasun is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: asqatasun AT asqatasun DOT org
 */
package jenkins.plugins.asqatasun;

import hudson.PluginWrapper;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jkowalczyk
 */
public class ProjectAsqatasunAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;
    private static final String URL_PREFIX_RESULT = 
            "home/contract/audit-synthesis.html?audit=";

    public ProjectAsqatasunAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    @Override
    public String getIconFileName() {
        PluginWrapper wrapper = Jenkins.getInstance().getPluginManager()
                .getPlugin(AsqatasunPlugin.class);
        return "/plugin/" + wrapper.getShortName() + "/images/Logo-Asqatasun.png";
    }

    @Override
    public String getDisplayName() {
        return "Asqatasun";
    }

    @Override
    public String getUrlName() {
        String webappUrl = Jenkins.getInstance().getDescriptorByType(AsqatasunRunnerBuilder.DescriptorImpl.class).getInstallation().getWebappUrl();
        if (project.getLastBuild() != null) {
            try {
                for (Object obj : FileUtils.readLines(project.getLastBuild().getLogFile())) {
                    String line = (String)obj;
                    if (StringUtils.startsWith(line, "Audit Id")) {
                        return buildAuditResultUrl(line, webappUrl);
                    }
                }
            }  catch (IOException ex) {
                Logger.getLogger(ProjectAsqatasunAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return webappUrl;
    }

    private String buildAuditResultUrl(String line, String webappUrl) {
        String auditId = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
        if (StringUtils.endsWith(webappUrl, "/")) {
            return webappUrl+URL_PREFIX_RESULT+auditId;
        } else {
            return webappUrl+"/"+URL_PREFIX_RESULT+auditId;
        }
    }
}

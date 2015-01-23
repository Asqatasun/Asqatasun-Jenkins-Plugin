/*
 *  Tanaguru Runner - Trigger Automated webpage assessmentfrom Jenkins 
 *  Copyright (C) 2008-2015  Tanaguru.org
 * 
 *  This file is part of Tanaguru.
 * 
 *  Tanaguru is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Contact us by mail: open-s AT open-s DOT com
 */
package jenkins.plugins.tanaguru;

import hudson.PluginWrapper;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jkowalczyk
 */
public class ProjectTanaguruAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;
    private static final String URL_PREFIX_RESULT = 
            "home/contract/audit-synthesis.html?audit=";

    public ProjectTanaguruAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    @Override
    public String getIconFileName() {
        PluginWrapper wrapper = Jenkins.getInstance().getPluginManager()
                .getPlugin(TanaguruPlugin.class);
        return "/plugin/" + wrapper.getShortName() + "/images/Logo-Tanaguru.png";
    }

    @Override
    public String getDisplayName() {
        return "Tanaguru";
    }

    @Override
    public String getUrlName() {
        String webappUrl = Jenkins.getInstance().getDescriptorByType(TanaguruRunnerBuilder.DescriptorImpl.class).getInstallation().getWebappUrl();
        if (project.getLastBuild() != null) {
            try {
                for (String line : FileUtils.readLines(project.getLastBuild().getLogFile())) {
                    if (StringUtils.startsWith(line, "Audit Id")) {
                        return buildAuditResultUrl(line, webappUrl);
                    }
                }
            }  catch (IOException ex) {
                Logger.getLogger(ProjectTanaguruAction.class.getName()).log(Level.SEVERE, null, ex);
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

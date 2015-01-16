/*
 * The MIT License
 *
 * Copyright 2015 jkowalczyk.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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

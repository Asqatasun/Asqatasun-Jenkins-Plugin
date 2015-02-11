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

import hudson.Util;
import hudson.util.Scrambler;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author jkowalczyk
 */
public class TanaguruInstallation {

    public static final TanaguruInstallation get() {
        TanaguruRunnerBuilder.DescriptorImpl tanaguruDescriptor = 
                Jenkins.getInstance().getDescriptorByType(TanaguruRunnerBuilder.DescriptorImpl.class);
        return tanaguruDescriptor.getInstallation();
    }

    private final String webappUrl;

    private final String databaseHost;
    private final String databasePort;
    private final String databaseName;
    private final String databaseLogin;
    private String databasePassword;

    private final String tanaguruLogin;

    @DataBoundConstructor
    public TanaguruInstallation(
            String webappUrl,
            String databaseHost,
            String databasePort,
            String databaseName,
            String databaseLogin,
            String databasePassword,
            String tanaguruLogin) {
        this.webappUrl = webappUrl;
        this.databaseHost = databaseHost;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.databaseLogin = databaseLogin;
        setDatabasePassword(databasePassword);
        this.tanaguruLogin = tanaguruLogin;
    }

    public String getWebappUrl() {
        return webappUrl;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    public String getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseLogin() {
        return databaseLogin;
    }
    
    public String getTanaguruLogin() {
        return tanaguruLogin;
    }
    
    public String getDatabasePassword() {
        return Scrambler.descramble(databasePassword);
    }

    public final void setDatabasePassword(String password) {
        this.databasePassword = Scrambler.scramble(Util.fixEmptyAndTrim(password));
    }

}
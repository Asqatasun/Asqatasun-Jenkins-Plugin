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
    private String tanaguruPassword;

    @DataBoundConstructor
    public TanaguruInstallation(
            String webappUrl,
            String databaseHost,
            String databasePort,
            String databaseName,
            String databaseLogin,
            String databasePassword,
            String tanaguruLogin,
            String tanaguruPassword) {
        this.webappUrl = webappUrl;
        this.databaseHost = databaseHost;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.databaseLogin = databaseLogin;
        setDatabasePassword(databasePassword);
        this.tanaguruLogin = tanaguruLogin;
        setTanaguruPassword(tanaguruPassword);
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

    /**
     * @return @since 2.0.1
     */
    public String getTanaguruPassword() {
        return Scrambler.descramble(tanaguruPassword);
    }

    public final void setDatabasePassword(String password) {
        this.databasePassword = Scrambler.scramble(Util.fixEmptyAndTrim(password));
    }

    public final void setTanaguruPassword(String tanaguruPassword) {
        this.tanaguruPassword = Scrambler.scramble(Util.fixEmptyAndTrim(tanaguruPassword));
    }
}

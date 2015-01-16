package jenkins.plugins.tanaguru;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import java.io.File;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link TanaguruRunnerBuilder} is created. The created instance is persisted
 * to the project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #name}) to remember the configuration.
 *
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 *
 * @author Jérôme Kowalczyk
 */
public class TanaguruRunnerBuilder extends Builder {

    private static final String TG_SCRIPT_NAME = "bin/tanaguru.sh";
    private final String scenario;
    private final String contractId;
    private final String refAndLevel;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public TanaguruRunnerBuilder(
            String scenario, 
            String refAndLevel, 
            String contractId) {
        this.scenario = scenario;
        this.refAndLevel = refAndLevel;
        this.contractId = contractId;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     *
     * @return
     */
    public String getScenario() {
        return scenario;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     *
     * @return
     */
    public String getRefAndLevel() {
        return refAndLevel;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.
        File contextDir = new File(getDescriptor().getTanaguruCliPath());
        if (!contextDir.exists()) {
            listener.getLogger().println("Le chemin vers le contexte d'exécution est incorrect");
            return false;
        }
        File scriptFile = new File(getDescriptor().getTanaguruCliPath() + "/" + TG_SCRIPT_NAME);
        if (!scriptFile.canExecute()) {
            listener.getLogger().println("Le script n'est pas exécutable");
            return false;
        }
        
        TanaguruRunner tanaguruRunner=
                new TanaguruRunner(
                        scenario, 
                        refAndLevel.split(";")[0],
                        refAndLevel.split(";")[1],
                        contextDir,
                        getDescriptor().getFirefoxPath(), 
                        getDescriptor().getDisplayPort(),
                        listener);

        tanaguruRunner.callTanaguruService();
        
        writeResultToWorkspace(tanaguruRunner, build.getWorkspace());
        
        linkToTanaguruWebapp(tanaguruRunner);
        
        return true;
    }
    
    /**
     * 
     * @param tanaguruRunner
     * @param workspace 
     */
    private void linkToTanaguruWebapp(
            TanaguruRunner tanaguruRunner) throws IOException, InterruptedException {


    }
    
    /**
     * 
     * @param tanaguruRunner
     * @param workspace 
     */
    private void writeResultToWorkspace(
            TanaguruRunner tanaguruRunner, 
            FilePath workspace) throws IOException, InterruptedException {
        
        File workspacedir = new File(workspace.toURI());
        
        File markFile = new File (workspacedir.getAbsolutePath()+"/tanaguru-mark.properties");
        FileUtils.write(markFile, "YVALUE="+tanaguruRunner.mark);
        
        File passedFile = new File (workspacedir.getAbsolutePath()+"/tanaguru-passed.properties");
        FileUtils.write(passedFile, "YVALUE="+tanaguruRunner.nbPassed);
        
        File failedFile = new File (workspacedir.getAbsolutePath()+"/tanaguru-failed.properties");
        FileUtils.write(failedFile, "YVALUE="+tanaguruRunner.nbFailed);
        
            File failedOccurencesFile = new File (workspacedir.getAbsolutePath()+"/tanaguru-failedOccurences.properties");
        FileUtils.write(failedOccurencesFile, "YVALUE="+tanaguruRunner.nbFailedOccurences);
        
        File nmiFile = new File (workspacedir.getAbsolutePath()+"/tanaguru-nmi.properties");
        FileUtils.write(nmiFile, "YVALUE="+tanaguruRunner.nbNmi);
        
        File naFile = new File (workspacedir.getAbsolutePath()+"/tanaguru-na.properties");
        FileUtils.write(naFile, "YVALUE="+tanaguruRunner.nbNa);
        
        File ntFile = new File (workspacedir.getAbsolutePath()+"/tanaguru-nt.properties");
        FileUtils.write(ntFile, "YVALUE="+tanaguruRunner.nbNt);
    }
    
    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new ProjectTanaguruAction(project);
    }

    /**
     * Descriptor for {@link TanaguruRunnerBuilder}. Used as a singleton. The
     * class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See
     * <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private String pathToCli = "";
        private String displayPort = "";
        private String firefoxPath = "";
        private TanaguruInstallation tanaguruInstallation;
        
        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value This parameter receives the value that the user has
         * typed.
         * @return Indicates the outcome of the validation. This is sent to the
         * browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message will
         * be displayed to the user.
         * @throws java.io.IOException
         * @throws javax.servlet.ServletException
         */
        public FormValidation doCheckScenario(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0) {
                return FormValidation.error("Please fill-in a path to a scenario");
            }
            if (value.length() < 4) {
                return FormValidation.warning("Isn't the name too short?");
            }
            return FormValidation.ok();
        }

        public ListBoxModel doFillRefAndLevelItems(@QueryParameter String selection) {
            return new ListBoxModel(
                    new Option("Accessiweb2.2 : Bronze", "Aw22;Bz", false),
                    new Option("Accessiweb2.2 : Argent", "Aw22;Ar", true),
                    new Option("Accessiweb2.2 : Or", "Aw22;Or", false),
                    new Option("Rgaa3 : Bronze", "Rgaa3;Bz", false),
                    new Option("Rgaa3 : Argent", "Rgaa3;Ar", false),
                    new Option("Rgaa3 : Or", "Rgaa3;Or", false)
            );
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        @Override
        public String getDisplayName() {
            return "Tanaguru Runner";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {

            pathToCli = formData.getString("tanaguruCliPath");
            displayPort = formData.getString("displayPort");
            firefoxPath = formData.getString("firefoxPath");
            tanaguruInstallation = 
                    new TanaguruInstallation(
                        formData.getString("webappUrl"), 
                        formData.getString("databaseHost"),
                        formData.getString("databasePort"),
                        formData.getString("databaseName"),
                        formData.getString("databaseLogin"),
                        formData.getString("databasePassword"),
                        formData.getString("tanaguruLogin"),
                        formData.getString("tanaguruPassword")
                    );

            save();
            return super.configure(req, formData);
        }

        /**
         * @return the path to the tanaguru cli script.
         */
        public String getTanaguruCliPath() {
            return pathToCli;
        }

        /**
         * @return the path to the tanaguru cli script.
         */
        public String getDisplayPort() {
            return displayPort;
        }

        /**
         * @return the path to the firefox instance.
         */
        public String getFirefoxPath() {
            return firefoxPath;
        }

        /**
         * @return all configured {@link hudson.plugins.sonar.SonarInstallation}
         */
        public TanaguruInstallation getInstallation() {
            return tanaguruInstallation;
        }
    }
}

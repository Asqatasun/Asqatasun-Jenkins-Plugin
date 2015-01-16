package jenkins.plugins.tanaguru;

import hudson.model.BuildListener;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 */
public class TanaguruRunner {

    private static final String TG_SCRIPT_NAME = "bin/tanaguru.sh";

    private final String scenario;
    private final String firefoxPath;
    private final String referential;
    private final String level;
    private final String displayPort;
    private final File contextDir;
    private final BuildListener listener;
    
    public String mark;
    public String nbPassed;
    public String nbFailed;
    public String nbFailedOccurences;
    public String nbNmi;
    public String nbNa;
    public String nbNt;
    
    public TanaguruRunner(
            String scenario, 
            String referential, 
            String level, 
            File contextDir,
            String firefoxPath,
            String displayPort,
            BuildListener listener) {
        this.scenario = scenario;
        this.referential = referential;
        this.level = level;
        this.firefoxPath = firefoxPath;
        this.displayPort = displayPort;
        this.contextDir = contextDir;
        this.listener = listener;
    }

    public void callTanaguruService() throws IOException, InterruptedException {
        File logFile = File.createTempFile("log-" + new Random().nextInt(), ".log");

        File scenarioFile = File.createTempFile("scenario-" + new Random().nextInt(), ".json");
        FileUtils.writeStringToFile(scenarioFile, scenario);

        ProcessBuilder pb = new ProcessBuilder(
                TG_SCRIPT_NAME,
                "-f", firefoxPath,
                "-r", referential,
                "-l", level,
                "-d", displayPort,
                "-t", "Scenario",
                scenarioFile.getAbsolutePath());

        pb.directory(contextDir);
        pb.redirectErrorStream(true);
        pb.redirectOutput(Redirect.appendTo(logFile));
        Process p = pb.start();
        p.waitFor();
        
        listener.getLogger().println(FileUtils.readFileToString(logFile));
        
        extractData(logFile);
        
        FileUtils.deleteQuietly(logFile);
        FileUtils.deleteQuietly(scenarioFile);
    }

    public void extractData(File logFile) throws IOException {
        for (String line : FileUtils.readLines(logFile)) {
            if (StringUtils.startsWith(line, "Subject")) {
                break;
            }
            if (StringUtils.startsWith(line, "RawMark")) {
                mark = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).replaceAll("%", "").trim();
            } else if (StringUtils.startsWith(line, "Nb Passed")) {
                nbPassed = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            } else if (StringUtils.startsWith(line, "Nb Failed test")) {
                nbFailed = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            } else if (StringUtils.startsWith(line, "Nb Failed occurences")) {
                nbFailedOccurences = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            } else if (StringUtils.startsWith(line, "Nb Pre-qualified")) {
                nbNmi = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            } else if (StringUtils.startsWith(line, "Nb Not Applicable")) {
                nbNa = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            } else if (StringUtils.startsWith(line, "Nb Not Tested")) {
                nbNt = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            }
            
        }
    }

    public String outputTanaguruResults() {
        return toString();
    }

}
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

    private final String tgScriptName;
    private final String scenario;
    private final String scenarioName;
    private final int buildNumber;
    private final String firefoxPath;
    private final String referential;
    private final String level;
    private final String displayPort;
    private final String xmxValue;
    private final File contextDir;
    private final BuildListener listener;
    
    public String auditId;
    public String mark;
    public String nbPassed;
    public String nbFailed;
    public String nbFailedOccurences;
    public String nbNmi;
    public String nbNa;
    public String nbNt;
    
    public TanaguruRunner(
            String tgScriptName,
            String scenarioName, 
            String scenario, 
            int buildNumber, 
            String referential, 
            String level, 
            File contextDir,
            String firefoxPath,
            String displayPort,
            String xmxValue,
            BuildListener listener) {
        this.tgScriptName = tgScriptName;    
        this.scenario = scenario;
        this.scenarioName = scenarioName;
        this.buildNumber = buildNumber;
        this.referential = referential;
        this.level = level;
        this.firefoxPath = firefoxPath;
        this.displayPort = displayPort;
        this.contextDir = contextDir;
        this.xmxValue = xmxValue;
        this.listener = listener;
    }

    public void callTanaguruService() throws IOException, InterruptedException {
        File logFile = createTempFile("log-" + new Random().nextInt()+".log", "");
        File scenarioFile = createTempFile(scenarioName+"_#"+buildNumber, scenario);

        ProcessBuilder pb = new ProcessBuilder(
                tgScriptName,
                "-f", firefoxPath,
                "-r", referential,
                "-l", level,
                "-d", displayPort,
                "-x", xmxValue,
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
            } else if (StringUtils.startsWith(line, "Audit Id")) {
                auditId = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            }
            
        }
    }

    public String outputTanaguruResults() {
        return toString();
    }

    /**
     * Create a temporary file within a temporary folder, created in the
     * contextDir if not exists (first time)
     * @param fileName
     * @param fileContent
     * @return
     * @throws IOException 
     */
    private File createTempFile(String fileName, String fileContent) throws IOException {
        File contextDirTemp = new File (contextDir.getAbsolutePath()+"/tmp");
        if (!contextDirTemp.exists()) {
            if (contextDirTemp.mkdir()) {
                contextDirTemp.setExecutable(true);
                contextDirTemp.setWritable(true);
            }
        }
        File tempFile = new File(contextDirTemp.getAbsolutePath()+"/"+fileName);
        FileUtils.writeStringToFile(tempFile, fileContent);
        return tempFile;
    }
}
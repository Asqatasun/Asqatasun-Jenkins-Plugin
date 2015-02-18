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
import java.io.PrintStream;
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
        File logFile = 
                TanaguruRunnerBuilder.createTempFile(
                        contextDir,
                        "log-" + new Random().nextInt()+".log", 
                        "");
        File scenarioFile = 
                TanaguruRunnerBuilder.createTempFile(
                        contextDir,
                        scenarioName+"_#"+buildNumber, 
                        TanaguruRunnerBuilder.forceVersion1ToScenario(scenario));

        ProcessBuilder pb = new ProcessBuilder(
                tgScriptName,
                "-f", firefoxPath,
                "-r", referential,
                "-l", level,
                "-d", displayPort,
                "-x", xmxValue,
                "-o", logFile.getAbsolutePath(),
                "-t", "Scenario",
                scenarioFile.getAbsolutePath());

        pb.directory(contextDir);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
        
        extractDataAndPrintOut(logFile, listener.getLogger());
        
        FileUtils.deleteQuietly(logFile);
        FileUtils.deleteQuietly(scenarioFile);
    }

    /**
     * 
     * @param logFile
     * @param ps
     * @throws IOException 
     */
    public void extractDataAndPrintOut(File logFile, PrintStream ps) throws IOException {
        ps.println("");
        boolean isFirstMark = true;
        boolean isFirstNbPassed = true;
        boolean isFirstNbFailed = true;
        boolean isFirstNbFailedOccurences = true;
        boolean isFirstNbNmi = true;
        boolean isFirstNbNa = true;
        boolean isFirstNbNt = true;
        for (String line : FileUtils.readLines(logFile)) {
            if (StringUtils.startsWith(line, "Subject")) {
                ps.println("");
                ps.println(line);
            } else if (StringUtils.startsWith(line, "Audit terminated")) {
                ps.println(line);
            } else if (StringUtils.startsWith(line, "RawMark")) {
                ps.println(line.replace("RawMark", "Mark"));
                if (isFirstMark) {
                    mark = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).replaceAll("%", "").trim();
                    isFirstMark = false;
                }
            } else if (StringUtils.startsWith(line, "Nb Passed")) {
                ps.println(line); 
                if (isFirstNbPassed) {
                  nbPassed = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
                    isFirstNbPassed = false;
                }
            } else if (StringUtils.startsWith(line, "Nb Failed test")) {
                ps.println(line);
                if (isFirstNbFailed) {
                   nbFailed = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
                   isFirstNbFailed = false;
                }
            } else if (StringUtils.startsWith(line, "Nb Failed occurences")) {
                ps.println(line);
                if (isFirstNbFailedOccurences) {
                    nbFailedOccurences = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
                    isFirstNbFailedOccurences = false;
                }
            } else if (StringUtils.startsWith(line, "Nb Pre-qualified")) {
                ps.println(line);
                if (isFirstNbNmi) {
                    nbNmi = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
                    isFirstNbNmi= false;
                }
            } else if (StringUtils.startsWith(line, "Nb Not Applicable")) {
                ps.println(line);
                if (isFirstNbNa) {
                    nbNa = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
                    isFirstNbNa = false;
                }
            } else if (StringUtils.startsWith(line, "Nb Not Tested")) {
                ps.println(line);
                if (isFirstNbNt) {
                    nbNt = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
                    isFirstNbNt = false;
                }
            } else if (StringUtils.startsWith(line, "Audit Id")) {
                ps.println(line);
                auditId = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            }
        }
        ps.println("");
    }

    public String outputTanaguruResults() {
        return toString();
    }

}
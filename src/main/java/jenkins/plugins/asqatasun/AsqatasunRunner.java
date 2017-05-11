/*
 * Asqatasun Runner - Trigger Automated webpage assessment from Jenkins 
 * Copyright (C) 2017 Asqatasun.org
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

import hudson.model.BuildListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 */
public class AsqatasunRunner {

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
    private final boolean isDebug;
    
    private String auditId;
    private String mark;
    private String nbPassed;
    private String nbFailed;
    private String nbFailedOccurences;
    private String nbNmi;
    private String nbNa;
    private String nbNt;
    
    public AsqatasunRunner(
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
            BuildListener listener,
            boolean isDebug) {
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
        this.isDebug = isDebug;
    }

    public void callService() throws IOException, InterruptedException {
        File logFile = 
                AsqatasunRunnerBuilder.createTempFile(
                        contextDir,
                        "log-" + new Random().nextInt()+".log", 
                        "");
        File scenarioFile = 
                AsqatasunRunnerBuilder.createTempFile(
                        contextDir,
                        scenarioName+"_#"+buildNumber, 
                        AsqatasunRunnerBuilder.forceVersion1ToScenario(scenario));

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
        listener.getLogger().print("Launching asqatasun runner with the following options : ");
        listener.getLogger().print(pb.command());
        Process p = pb.start();
        p.waitFor();
        
        extractDataAndPrintOut(logFile, listener.getLogger());
        
        if (!isDebug) {
            FileUtils.forceDelete(logFile);
        }
        
        FileUtils.forceDelete(scenarioFile);
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
        for (Object obj : FileUtils.readLines(logFile)) {
            String line = (String)obj;
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
                nbPassed = getNbStatus(ps, isFirstNbPassed, line);
                isFirstNbPassed = false;
            } else if (StringUtils.startsWith(line, "Nb Failed test")) {
                nbFailed = getNbStatus(ps, isFirstNbFailed, line);
                isFirstNbFailed = false;
            } else if (StringUtils.startsWith(line, "Nb Failed occurences")) {
                nbFailedOccurences = getNbStatus(ps, isFirstNbFailedOccurences, line);
                isFirstNbFailedOccurences = false;
            } else if (StringUtils.startsWith(line, "Nb Pre-qualified")) {
                nbNmi = getNbStatus(ps, isFirstNbNmi, line);
                isFirstNbNmi = false;
            } else if (StringUtils.startsWith(line, "Nb Not Applicable")) {
                nbNa = getNbStatus(ps, isFirstNbNa, line);
                isFirstNbNa = false;
            } else if (StringUtils.startsWith(line, "Nb Not Tested")) {
                nbNt = getNbStatus(ps, isFirstNbNt, line);
                isFirstNbNt = false;
            } else if (StringUtils.startsWith(line, "Audit Id")) {
                ps.println(line);
                auditId = StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
            }
        }
        ps.println("");
    }

    private String getNbStatus(PrintStream ps, boolean isFirstNb, String line) {
        ps.println(line);
        if (isFirstNb) {
           return StringUtils.substring(line, StringUtils.indexOf(line, ":")+1).trim();
        }
        return null;
    }

    public String outputAsqatasunResults() {
        return toString();
    }

    public String getAuditId() {
        return auditId;
    }

    public String getMark() {
        return mark;
    }

    public String getNbPassed() {
        return nbPassed;
    }

    public String getNbFailed() {
        return nbFailed;
    }

    public String getNbFailedOccurences() {
        return nbFailedOccurences;
    }

    public String getNbNmi() {
        return nbNmi;
    }

    public String getNbNa() {
        return nbNa;
    }

    public String getNbNt() {
        return nbNt;
    }
}
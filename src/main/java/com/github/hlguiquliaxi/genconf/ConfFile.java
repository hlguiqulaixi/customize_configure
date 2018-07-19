package com.github.hlguiquliaxi.genconf;

import java.io.*;
import java.util.HashMap;

public class ConfFile {
    String fileName;
    String localFileName;
    String localFileAbsPath;
    String dstHost;
    String dstPathRoot;

    final HashMap<String, String> valuesMap = new HashMap<String, String>();

    ConfFile(String fileName, String dstHost, String dstPathRoot){
        this.fileName = fileName;
        this.dstHost = dstHost;
        this.dstPathRoot = dstPathRoot;
        this.localFileName = fileName + "_" + dstHost;
    }

    public boolean isMarked(String line){
        if(line.contains("@{") && line.contains("}@")){
            return true;
        }
        return false;
    }

    public String genRealValue(String line) throws IOException {
        int startPos = line.indexOf("@{");
        int endPos = line.indexOf("}@");
        if(startPos < 0 || endPos < 0){
            return line;
        }
        String jsonStr = line.substring(startPos+1, endPos+1);
        Variable var = new Variable(jsonStr);
        var.parseJsonStr();

        if(valuesMap.containsKey(var.name)){
            var.readValue(valuesMap);
        }
        else{
            Variable.MannualInput.readValue(this, var);
        }

        return line.substring(0, startPos) + var.value + line.substring(endPos + 2);
    }

    public void genConf(String templateFilePath) throws IOException {
        String line = null;
        String newLine = null;
        BufferedReader in = new BufferedReader(new FileReader(templateFilePath));
        File outFile = new File(localFileName);
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        while((line = in.readLine()) != null){
            if(isMarked(line)){
                newLine = genRealValue(line);
            }
            else{
                newLine = line;
            }
            out.write(newLine);
            out.newLine();
        }
        out.close();
        in.close();

        localFileAbsPath = outFile.getAbsolutePath();
    }

    public void sendToDstHost() {
        String cmd = "scp " + "'" + localFileAbsPath + "' 'noah@" + dstHost
                + ":" + dstPathRoot + "/" + fileName + "'";
        RunSystemCommand.runSystemCmd(cmd);
    }
}



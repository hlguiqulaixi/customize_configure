package com.github.hlguiquliaxi.genconf;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FilenameUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import gnu.getopt.Getopt;

class ClusterHost{
    String addr;
    String dstConfRoot;

    ClusterHost(String addr, String dstConfRoot){
        this.addr = addr;
        this.dstConfRoot = dstConfRoot;
    }
}

public class GenerateConf {
    public ArrayList<ClusterHost> clusterHostList = new ArrayList<ClusterHost>();

    public void readClusterHostsInfo(String clusterConfig) throws IOException {
        File file = new File(clusterConfig);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String jstr = new String(data, "UTF-8");

        JSONObject jobj = new JSONObject(jstr);
        JSONArray jarr = jobj.getJSONArray("hosts");
        Iterator iterator = jarr.iterator();
        while(iterator.hasNext()){
            JSONObject hostInfo = (JSONObject)iterator.next();
            clusterHostList.add(new ClusterHost(hostInfo.getString("addr"), hostInfo.getString("dstConfRoot")));
        }
    }

    public void generateConf(String templateFilePath) throws IOException {
        String fileName = FilenameUtils.getName(templateFilePath);
        for(ClusterHost host: clusterHostList){
            System.out.println();
            System.out.println("------Try to generate configure file: " + host.addr + " <-------> " + fileName);
            ConfFile confFile = new ConfFile(fileName, host.addr, host.dstConfRoot);
            confFile.genConf(templateFilePath);
            confFile.sendToDstHost();
        }

    }

    public void usage(){
        System.out.println("--This program need two arguments, such as:");
        System.out.println("  java -jar genconf.jar -d /home/hello/template.yaml -c /home/hello/cluster.info");
        System.out.println("  -d: The path of a template file");
        System.out.println("  -c: The path of a cluster configure file. The cluster configure file is comprised of ");
        System.out.println("      host info and must be in json format.");
    }

    public static void main(String[] args) throws IOException {
        String templateFile = null;
        String clusterConfig = null;
        GenerateConf generateConf = new GenerateConf();

        if(args.length < 4){
            System.out.println("--Error: Invalid parameters!");
            generateConf.usage();
            return;
        }

        Getopt testOpt  = new Getopt(args[0], args, "t:c:");
        int res;
        while( (res = testOpt.getopt()) != -1 ) {
            switch (res) {
                case 't':
                    templateFile = testOpt.getOptarg();
                    System.out.println("temlateFile: " + templateFile);
                    break;
                case 'c':
                    clusterConfig = testOpt.getOptarg();
                    System.out.println("clusterConfig: " + clusterConfig);
                    break;
                default:
                    break;
            }
        }

        System.out.println(Arrays.toString(args));
        System.out.println("templateFilePath: " + templateFile);
        System.out.println("clusterConfig: " + clusterConfig);

        generateConf.readClusterHostsInfo(clusterConfig);
        generateConf.generateConf(templateFile);
    }
}

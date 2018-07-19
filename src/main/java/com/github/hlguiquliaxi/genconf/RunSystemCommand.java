package com.github.hlguiquliaxi.genconf;

import java.io.*;
import java.util.ArrayList;

public class RunSystemCommand {
    static ArrayList<String> stdOutInfoList = new ArrayList<>();
    static ArrayList<String> stdErrInfoList = new ArrayList<>();

    static void runSystemCmd(String cmd) {
        String s = null;
        stdErrInfoList.clear();
        stdOutInfoList.clear();

        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        System.out.println("---cmd: " + cmd);

        try {
            Process p = pb.start();

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                stdOutInfoList.add(s);
            }

            while ((s = stdError.readLine()) != null) {
                stdErrInfoList.add(s);
            }

            int extVal = p.waitFor();
            if(0 != extVal ){
                System.out.println("---cmd failed: " + cmd);
                System.out.println("---std error:");
                for(String tmp: stdErrInfoList){
                    System.out.println(tmp);
                }
            }
            else{
                System.out.println("---cmd success: " + cmd);
            }
        } catch (IOException e) {
            System.out.println("IOException happened: ");
            e.printStackTrace();
            // System.exit(-1);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException, Cmd-execution has been interrupted:");
            e.printStackTrace();
        }
    }

    static void runSystemCmdNoWait(String cmd) {
        String s = null;
        stdErrInfoList.clear();
        stdOutInfoList.clear();

        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        System.out.println("---cmd: " + cmd);

        try {
            Process p = pb.start();

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));


            System.out.println("---stdout:");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            System.out.println("---stderr:");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("IOException happened: ");
            e.printStackTrace();
            // System.exit(-1);
        }
    }
}

package com.github.hlguiquliaxi.genconf;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Variable {
    public String jsonStr;
    public String name;
    public String desc;
    public String defaultVal;
    public String value;

    Variable(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    void parseJsonStr() {
        JSONObject jobj = new JSONObject(jsonStr);
        if (jobj.has("n")) {
            name = jobj.getString("n");
        } else {
            name = null;
        }

        if (jobj.has("d")) {
            desc = jobj.getString("d");
        }

        if (jobj.has("dv")) {
            defaultVal = jobj.getString("dv");
        }
    }

    void readValue(Map<String, String> map) {
        if (map.containsKey(name)) {
            value = map.get(name);
        }
    }

    public static class MannualInput {
        public static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        static void readValue(ConfFile confFile, Variable var) throws IOException {
            System.out.println("Please input the value:");
            System.out.println("---host:          " + confFile.dstHost);
            System.out.println("---item:          " + var.name);
            System.out.println("---desc:          " + var.desc);
            System.out.println("---default-value: " + var.defaultVal);
            var.value = stdin.readLine();
        }
    }

}


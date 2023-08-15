package com.temah.lam.exec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ExecUtils {
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static long byteToGB(long size){
        return size / 1024 / 1024 / 1024;
    }

    public static String getLocalHost() {
        String hostAddress = null;
        try {
            hostAddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        }
        return hostAddress;
    }

    public static String getInString(InputStream is, Charset ouputCharset) throws Exception {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, ouputCharset == null ? StandardCharsets.UTF_8 : ouputCharset));
        String line;
        while ((line = reader.readLine()) != null) {
            if (result.length() > 0) {
                result.append(System.lineSeparator());
            }
            result.append(line);
        }
        return result.toString();
    }

    public static double extractLinuxUsedCpu(String cpuResult) {
        double value = -1.0;
        if (cpuResult != null && cpuResult.contains("Cpu") && cpuResult.contains("us")) {
            String num = cpuResult.substring(cpuResult.indexOf(":")+1, cpuResult.indexOf("us"));
            value = Double.parseDouble(num.trim());
        }
        return value;
    }

    public static double extractLinuxUsedMemory(String memResult) {
        double value = -1.0;
        if (memResult != null && memResult.contains("MiB Mem") && memResult.contains("total")) {
            String total = memResult.substring(memResult.indexOf(":") + 1, memResult.indexOf("total"));
            double totalMem = Double.parseDouble(total.trim());
            String used = memResult.substring(memResult.indexOf("free,") + 5, memResult.indexOf("used,"));
            double usedMem = Double.parseDouble(used.trim());
            value = (usedMem / totalMem) * 100;
        }
        return value;
    }
}

package com.temah.lam.collector;

import com.sun.management.OperatingSystemMXBean;
import com.temah.lam.exec.ExecUtils;
import com.temah.lam.exec.RuntimeExec;

import java.lang.management.ManagementFactory;

/**
 * 获取当前环境的CPU和内存占用信息
 */
public class LocalPlatformStateCollector {

    public static String getResultFromWmicGetOutput(String cmd, String ignoreLine) {
        String res = null;
        String output = RuntimeExec.exec(cmd, null);
        if (output != null && !output.isEmpty()) {
            String[] lines = output.split("[\r\n]+");
            for (String line : lines) {
                if (line == null) {
                    continue;
                }
                String resultLine = line.trim();
                if (!resultLine.isEmpty() && !resultLine.contains(ignoreLine)) {
                    res = resultLine;
                    break;
                }
            }
        }
        return res;
    }

    public static double getUsedCpuWithCMD() {
        double value = -1.0;
        if (ExecUtils.isWindows()) {
            String cm = "wmic cpu get LoadPercentage";
            String cpuResult = getResultFromWmicGetOutput(cm, "LoadPercentage");
            if (cpuResult != null && !cpuResult.isEmpty()) {
                value = Integer.parseInt(cpuResult);
            }
        } else if (ExecUtils.isLinux()) {
            String cm = "top -b -n 1 | grep Cpu";
            String cpuResult = RuntimeExec.exec(cm, null);
            value = ExecUtils.extractLinuxUsedCpu(cpuResult);
        }
        return value;
    }

    public static Double getUsedMemoryWithCMD(){
        double value = -1.0;

        if (ExecUtils.isWindows()) {
            String cm = "wmic os get TotalVisibleMemorySize";
            String totalMemResult = getResultFromWmicGetOutput(cm, "TotalVisibleMemorySize");
            cm = "wmic os get FreePhysicalMemory";
            String freeMemResult = getResultFromWmicGetOutput(cm, "FreePhysicalMemory");
            long totalMem = 0L;
            long freeMem = 0L;
            if (totalMemResult != null && !totalMemResult.isEmpty()) {
                totalMem = Long.parseLong(totalMemResult);
            }
            if (freeMemResult != null && !freeMemResult.isEmpty()) {
                freeMem = Long.parseLong(freeMemResult);
            }
            if (totalMem > 0L) {
                value = ((totalMem - freeMem) * 1.0 / totalMem) * 100;
            }
        } else if (ExecUtils.isLinux()) {
            String cm = "top -b -n 1 | grep 'MiB Mem'";
            String memResult = RuntimeExec.exec(cm, null);
            value = ExecUtils.extractLinuxUsedMemory(memResult);
        }
        return value;
    }

    public static double getUsedCpu() {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double usedCpu = mem.getSystemCpuLoad();
        // 获取失败，或出现异常
        if (usedCpu < 0.0) {
            usedCpu = 0.0;
        }
        return usedCpu;
    }

    public static double getUsedMemory() {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalMemorySize = mem.getTotalPhysicalMemorySize(); // bytes
        long freeMemorySize = mem.getFreePhysicalMemorySize(); // bytes
        return ((totalMemorySize - freeMemorySize) * 1.0 / totalMemorySize) * 100;
    }

    public static void main(String[] args) {
        System.out.println(getUsedCpu());
        System.out.println(getUsedMemory());
    }
}

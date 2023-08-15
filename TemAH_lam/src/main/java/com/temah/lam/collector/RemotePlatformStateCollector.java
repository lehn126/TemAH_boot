package com.temah.lam.collector;

import com.temah.lam.exec.ExecUtils;
import com.temah.lam.exec.RemoteExec;

public class RemotePlatformStateCollector {
    public static double getUsedCpuWithCMD(String user, String passwd, String host, int port) {
        String cm = "top -b -n 1 | grep Cpu";
        String cpuResult = RemoteExec.exec(cm, user, passwd, host, port, null);
        return ExecUtils.extractLinuxUsedCpu(cpuResult);
    }

    public static Double getUsedMemoryWithCMD(String user, String passwd, String host, int port) {
        String cm = "top -b -n 1 | grep 'MiB Mem'";
        String memResult = RemoteExec.exec(cm, user, passwd, host, port, null);
        return ExecUtils.extractLinuxUsedMemory(memResult);
    }

    public static void main(String[] args) {
        System.out.println(getUsedCpuWithCMD("carl", "123456", "192.168.2.129", 22));
        System.out.println(getUsedMemoryWithCMD("carl", "123456", "192.168.2.129", 22));
    }
}

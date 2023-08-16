package com.temah.lam.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.Charset;

public class RuntimeExec {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeExec.class);

    public static String exec(String cm, Charset ouputCharset) {
        String os = System.getProperty("os.name").toLowerCase();
        Process ps = null;
        InputStream is = null;
        String result = null;
        try {
            if (os.contains("win")) {
                String[] cmd = {"cmd", "/c", cm};
                ps = Runtime.getRuntime().exec(cmd);
            } else if (os.contains("linux")) {
//                if(cm.contains("rm ")){
//                    System.err.println("不能执行危险命令"+cm);
//                    return result;
//                }
//                if(cm.contains("mv ")){
//                    System.err.println("不能执行危险命令"+cm);
//                    return result;
//                }
                String[] cmd = {"/bin/sh", "-c", cm};
                ps = Runtime.getRuntime().exec(cmd);
            }
            if (ps != null) {
                is = ps.getInputStream();
                result = ExecUtils.getInString(is, ouputCharset);
            }

            logger.info("执行命令: {} 输出--<{}>--", cm, result);
        } catch (Exception e) {
            logger.error("执行命令遇到错误: {}", e.getMessage(), e);
        } finally {
            try {
                if (null != ps) {
                    ps.waitFor();
                }
                if (null != is) {
                    is.close();
                }
                if (null != ps) {
                    ps.destroy();
                }
            } catch (Exception e) {
                logger.error("关闭输入流遇到错误: {}", e.getMessage(), e);
            }
        }
        return result;
    }

}

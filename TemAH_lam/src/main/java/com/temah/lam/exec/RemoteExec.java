package com.temah.lam.exec;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 通过SSH连接到远程服务器执行命令
 */
public class RemoteExec {

    private static final Logger logger = LoggerFactory.getLogger(RemoteExec.class);

    /**
     * 连接到指定的HOST
     */
    private static Session connect(String user, String passwd, String host, int port) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(passwd);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
        } catch (JSchException e) {
            logger.error("远程连接错误: [{}:{}] {}", host, port, e.getMessage(), e);
            if (session != null) {
                try {
                    session.disconnect();
                } catch (Exception e1) {
                    logger.error("关闭远程连接遇到错误: [{}:{}] {}", host, port, e1.getMessage(), e1);
                }
            }
            session = null;
        }
        return session;
    }

    public static String exec(String cm, String user, String passwd, String host, int port, Charset ouputCharset) {
        Session session = connect(user, passwd, host, port);
        if (session == null) {
            return null;
        }
        Channel channel = null;
        InputStream is = null;
        String result = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cm);

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            channel.connect();
            is = channel.getInputStream();
            result = ExecUtils.getInString(is, ouputCharset);

            logger.info("执行远程命令: [{}:{}] {} 输出--<{}>--", host, port, cm, result);
        } catch (Exception e) {
            logger.error("执行远程命令遇到错误: [{}:{}] {}", host, port, e.getMessage(), e);
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != channel) {
                    channel.disconnect();
                }
                session.disconnect();
            } catch (Exception e) {
                logger.error("关闭远程连接遇到错误: [{}:{}] {}", host, port, e.getMessage(), e);
            }
        }
        return result;
    }
}

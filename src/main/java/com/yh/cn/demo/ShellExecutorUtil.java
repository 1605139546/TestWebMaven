package com.yh.cn.demo;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ShellExecutorUtil {
    private Connection conn;
    private String ip;
    private String username;
    private String password;
    //字符集
    private String charset = Charset.defaultCharset().toString();
    //超时30minute
    private static final int TIME_OUT = 1000 * 30 * 60;

    /**
     * 构造器
     *
     * @param ip
     * @param username
     * @param pasword
     */
    public ShellExecutorUtil(String ip, String username, String pasword) {
        this.ip = ip;
        this.username = username;
        this.password = pasword;
    }

    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(username, password);
    }

    /**
     * 使用流处理执行linux命令输出到屏幕的数据
     *
     * @param in
     * @param charset
     * @return
     * @throws Exception
     */
    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    /**
     * 执行脚本
     *
     * @param cmds linux命令 or shell脚本
     * @return
     * @throws Exception
     */
    public int exec(String cmds) throws Exception {
        //linux中标准输出
        InputStream stdOut = null;
        //linux中标准错误输出
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        //linux命令返回值：即$?
        int ret = -1;
        try {
            if (login()) {
                //开启一个会话
                Session session = conn.openSession();
                //执行命令
                session.execCommand(cmds);
                //获取一个标准输出
                stdOut = new StreamGobbler(session.getStdout());
                //通过通用方法转化成字符串
                outStr = processStream(stdOut, charset);
                //获取一个标准错误输出
                stdErr = new StreamGobbler(session.getStderr());
                //通过通用方法转化成字符串
                outErr = processStream(stdErr, charset);
                //设置超时
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                //命令返回值 $?
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录" + ip + "失败!");
            }
        } finally {
            if (conn != null) conn.close();
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        System.out.println("outStr=" + outStr);
        System.out.println("outErr=" + outErr);
        System.out.println("ret=" + ret);
        return ret;
    }

    public static void main(String args[]) throws Exception {
        ShellExecutorUtil executor = new ShellExecutorUtil("192.168.182.132", "root", "123456");
        System.out.println(executor.exec("/usr/local/test.sh)"));
    }
}
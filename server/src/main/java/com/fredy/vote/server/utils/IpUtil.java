package com.fredy.vote.server.utils;

import org.apache.shiro.authc.UnknownAccountException;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author Fredy
 * @date 2020-11-26 16:26
 */
public class IpUtil {

    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ipAddress = null;

        ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                inet = InetAddress.getLocalHost();
                ipAddress = inet.getHostAddress();
            }
        }

        if (ipAddress != null && ipAddress.length() > 15) {
            if(ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }

        return ipAddress;
    }
}

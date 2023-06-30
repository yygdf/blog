package com.iksling.blog.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class IpUtil {
    private static String url;

    private static String ak;

    private static String coor;

    @Value("${baidu.url}")
    public void setUrl(String url) {
        IpUtil.url = url;
    }

    @Value("${baidu.ak}")
    public void setAk(String ak) {
        IpUtil.ak = ak;
    }

    @Value("${baidu.coor}")
    public void setCoor(String coor) {
        IpUtil.coor = coor;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-real-ip");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
                ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
                ipAddress = request.getHeader("Proxy-Client-IP");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    assert inet != null;
                    ipAddress = inet.getHostAddress();
                }
            }
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0)
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    public static String getIpSource(String ipAddress) {
        try {
            URL requestUrl = new URL(url + "?ak=" + ak + "&ip=" + ipAddress + "&coor=" + coor);
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestUrl.openConnection().getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null)
                result.append(line);
            reader.close();
            Map map = JSON.parseObject(result.toString(), Map.class);
            if (map.get("status").equals(0)) {
                Map m = (Map) map.get("content");
                Object province = ((Map) m.get("address_detail")).get("province");
                Object city = ((Map) m.get("address_detail")).get("city");
                return String.valueOf(province) + city;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}

package com.yihu.base.router.util;

import org.springframework.util.StringUtils;

/**
 * Created by chenweida on 2018/5/31 0031.
 */


public class IpV4Util {
    /**
     *
     *支持单个和网段
     * @param ip
     * @param ipSection   10.10.1.2-11.10.1.2 或者10.10.1.2
     * @return
     */
    public static boolean ipExistsInRange(String ip, String ipSection) {

        ipSection = ipSection.trim();
        ip = ip.trim();
        String[] ipSections = ipSection.split("-");
        if (ipSections.length == 1) {
            return org.apache.commons.lang.StringUtils.equals(ip, ipSections[0]);
        }
        int idx = ipSection.indexOf('-');
        String beginIP = ipSection.substring(0, idx);
        String endIP = ipSection.substring(idx + 1);
        return getIp2long(beginIP) <= getIp2long(ip) && getIp2long(ip) <= getIp2long(endIP);

    }

    private static long getIp2long(String ip) {

        ip = ip.trim();

        String[] ips = ip.split("\\.");

        long ip2long = 0L;

        for (int i = 0; i < 4; ++i) {

            ip2long = ip2long << 8 | Integer.parseInt(ips[i]);

        }

        return ip2long;

    }

    private static long getIp2long2(String ip) {

        ip = ip.trim();

        String[] ips = ip.split("\\.");

        long ip1 = Integer.parseInt(ips[0]);

        long ip2 = Integer.parseInt(ips[1]);

        long ip3 = Integer.parseInt(ips[2]);

        long ip4 = Integer.parseInt(ips[3]);


        long ip2long = 1L * ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;

        return ip2long;

    }

    public static void main(String[] args) {

        //10.10.10.116 是否属于固定格式的IP段10.10.1.00-10.10.255.255

        String ip = "11.10.1.1";

        String ipSection = "10.10.1.2-11.10.1.2";

        boolean exists = ipExistsInRange(ip, ipSection);

        System.out.println(exists);
    }

}


package com.xyj.common.tool;

/**
 * @className:IPUtil.java
 * @classDescription:ip转换
 * @author:xiayingjie
 * @createTime:2010-12-18
 */

public class IPUtil {
    /**
     * 把ip字符串转换为long型
     *
     * @param ip
     * @return long
     */
    private static long ip2long(String ip) {
        String[] arr = ip.split("\\.");
        return (Long.valueOf(arr[0]) * 0x1000000 + Long.valueOf(arr[1])
                * 0x10000 + Long.valueOf(arr[2]) * 0x100 + Long.valueOf(arr[3]));
    }

    /**
     * 将ip long转换成String
     *
     * @param ip
     * @return String
     */
    private static String long2ip(long ip) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ip >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ip & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ip & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ip & 0x000000FF)));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(ip2long("117.135.147.73"));
        System.out.println(long2ip(1971819337));
    }
}

package com.plugin.utils;

public class CommonUtils {
    /**
     * 规范化前缀：确保以 "/" 开头，不以 "/" 结尾（根 "/" 除外）
     *
     * @param prefix 路径前缀
     * @return 规范化后的路径前缀
     */
    protected static String normalizePrefix(String prefix) {
        String p = prefix.trim();
        if (!p.startsWith("/")) {
            p = "/" + p;
        }
        // 保留根 "/"，其他结尾 "/" 去掉
        if (p.length() > 1 && p.endsWith("/")){
            p = p.substring(0, p.length() - 1);
        }
        return p;
    }
}

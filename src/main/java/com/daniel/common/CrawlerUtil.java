package com.daniel.common;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 爬虫辅助工具类
 *
 * @author lingengxiang
 * @date 2018/12/11 10:50
 */
public class CrawlerUtil {

    /**
     * 计算分页页数
     *
     * @param totalRecord 总记录数目
     * @param pageSize    每页记录数
     * @return 页数
     */
    public static Integer calculatePageNum(int totalRecord, int pageSize) {
        return (totalRecord + pageSize - 1) / pageSize;
    }


    /**
     * 获取URL中的指定参数的值
     *
     * @param url URL
     * @param key 指定参数
     * @return 指定参数的值，如果无则返回空字符串
     */
    public static String getUrlParam(String url, String key) {
        String paramStr = url.substring(url.indexOf("?") + 1);
        for (String param : paramStr.split("&")) {
            String[] keyAndValue = param.split("=");
            if (keyAndValue.length > 1 && keyAndValue[0].equals(key)) {
                return keyAndValue[1];
            }
        }
        return "";
    }


    public static String parseData(String topCategory, String subCategory, JSONObject jsonData) {
        return String.format(CrawlerConstant.DATA_OUTPUT_FORMAT,
                topCategory,
                subCategory,
                jsonData.getString("name"),
                jsonData.getString("address"),
                jsonData.getJSONObject("location").getString("lng"),
                jsonData.getJSONObject("location").getString("lat"),
                jsonData.getString("province"),
                jsonData.getString("city"),
                jsonData.getString("area"));
    }


    public static String parseData(String topCategory, String subCategory, JSONArray results) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            JSONObject result = results.getJSONObject(i);
            data.append(parseData(topCategory, subCategory, result));
            if (i != results.size() - 1) {
                data.append("\r\n");
            }
        }
        return data.toString();
    }
}

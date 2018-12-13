package com.daniel.common;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 爬虫辅助工具类
 *
 * @author lingengxiang
 * @date 2018/12/11 10:50
 */
public class CrawlerUtil {

    /**
     * 根据抓取地区构建URL
     *
     * @param regions 目标抓取地
     * @return 存放抓取地区首页URL的List
     */
    public static List<String> loadUrl(String[] regions, String coordType) {
        List<String> urls = new LinkedList<>();
        File file = new File(CrawlerConstant.TAG_PATH);
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            // 逐行读取
            while ((line = bufferedReader.readLine()) != null) {
                String[] categories = line.split(CrawlerConstant.TAG_TOP_SPLIT);
                if (categories.length > 1) {
                    String[] tags = categories[1].split(CrawlerConstant.TAG_SUB_SPLIT);
                    // 通过标签以及抓取区域构建URL
                    for (String region : regions) {
                        for (String tag : tags) {
                            urls.add(String.format(CrawlerConstant.BAIDU_MAP_API,
                                    // 检索关键词，一级分类，抓取地区，坐标类型，用户AK
                                    tag, categories[0], region, coordType, CrawlerConstant.BAIDU_MAP_API_AK));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }


    /**
     * 创建文件
     *
     * @param path 路径路径
     * @return 创建的文件
     */
    public static File createFile(String path) {
        File output = new File(path);
        // 如果文件不存在，则先创建新文件
        try {
            if (!output.exists()) {
                output.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }


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

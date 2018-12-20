package com.daniel.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

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

    /**
     * 将一条JSON数据转换为指定的格式
     *
     * @param topCategory 一级分类
     * @param subCategory 二级分类
     * @param jsonData    JSON数据
     * @return 指定格式字符串
     */
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

    /**
     * 将多条JSON数据转换为指定的格式
     *
     * @param topCategory 一级分类
     * @param subCategory 二级分类
     * @param results     JSON数组
     * @return 指定格式字符串
     */
    public static String parseData(String topCategory, String subCategory, JSONArray results) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            JSONObject result = results.getJSONObject(i);
            data.append(parseData(topCategory, subCategory, result)).append("\r\n");
        }
        return data.toString();
    }

    /**
     * 读取整个文件
     *
     * @param path 文件路径
     * @return 文件字符串
     */
    public static String readFile(String path) {
        StringBuilder data = new StringBuilder();
        //通过将给定路径名字符串转换为抽象路径名来创建一个新 File 实例。
        File file = new File(path);
        try {
            //创建一个使用默认字符集的 InputStreamReader。
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            //创建一个使用默认大小输入缓冲区的缓冲字符输入流。
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                //一次读入一行数据
                data.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    /**
     * 读取区域数据
     *
     * @param path 文件路径
     * @return 区域数据
     */
    public static Map<String, Map<String, List<String>>> readAreaData(String path) {
        String data = CrawlerUtil.readFile(path);
        JSONArray dataJson = JSON.parseArray(data);

        // 用于存储地区数据，Map<省,Map<市,区的List>>
        Map<String, Map<String, List<String>>> areaData = new LinkedHashMap<>();
        // 遍历每个一级辖区
        for (int i = 0; i < dataJson.size(); i++) {
            JSONObject provinceJson = dataJson.getJSONObject(i);

            // 读取该一级辖区下的所有城市的JSON数据
            JSONArray citiesJson = provinceJson.getJSONArray("city");
            Map<String, List<String>> cities = new LinkedHashMap<>();
            // 遍历该一级辖区下的所有城市
            for (int j = 0; j < citiesJson.size(); j++) {
                JSONObject cityJson = citiesJson.getJSONObject(j);
                // 读取该城市下的所有区
                JSONArray areasJson = cityJson.getJSONArray("area");
                List<String> areas = areasJson.toJavaList(String.class);
                cities.put(cityJson.getString("name"), areas);
            }
            areaData.put(provinceJson.getString("name"), cities);
        }

        return areaData;
    }

    /**
     * 构建完整地区名
     *
     * @param province 省
     * @param city     市
     * @param area     区
     * @param areaData 级联地区数据
     * @return
     */
    public static String[] getRegionArray(String province, String city, String area, Map<String, Map<String, List<String>>> areaData) {

        // 如果city与area为空，则构建整个省份的数据
        if (StringUtils.isBlank(city) && StringUtils.isBlank(area)) {
            List<String> areas = new LinkedList<>();
            for (Map.Entry<String, List<String>> cityMap : areaData.get(province).entrySet()) {
                String fullArea = province + cityMap.getKey();
                for (String areaStr : cityMap.getValue()) {
                    areas.add(fullArea + areaStr);
                }
            }
            return areas.toArray(new String[0]);
        }

        // 如果只有area为空，则构建整个市的数据
        if (StringUtils.isNotBlank(city) && StringUtils.isBlank(area)) {
            List<String> areas = new LinkedList<>();
            String fullArea = province + city;
            for (String areaStr : areaData.get(province).get(city)) {
                areas.add(fullArea + areaStr);
            }
            return areas.toArray(new String[0]);
        }

        // 如果都不为空，则只返回该区的数据
        return new String[]{province + city + area};
    }
}

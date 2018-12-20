package com.daniel.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daniel.common.CrawlerUtil;
import com.daniel.entity.Area;

import java.util.LinkedList;
import java.util.List;

/**
 * @author lingengxiang
 * @date 2018/12/18 11:28
 */
public class TestData {

    public static void main(String[] args) {
        String path = "D:\\city.txt";
        String data = CrawlerUtil.readFile(path);
        JSONArray areas = JSON.parseArray(data);
        for (int i = 0; i < areas.size(); i++) {
            Area province = new Area();
            JSONObject provinceJson = areas.getJSONObject(i);
            province.setName(provinceJson.getString("name"));

            List<Area> cities = new LinkedList<>();
            JSONArray citiesJson = provinceJson.getJSONArray("city");
            for (int j = 0; j < citiesJson.size(); j++) {
                Area city = new Area();
                JSONObject cityJson = citiesJson.getJSONObject(i);
                city.setName(cityJson.getString("name"));

            }
        }
        System.out.println(areas.size());
    }
}

package com.daniel.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daniel.entity.Poi;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingengxiang
 * @date 2018/12/10 12:45
 */
public class TestBaidu {

    public static void main(String[] args) throws IOException, InterruptedException {

        int pageSize = 20;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://api.map.baidu.com/place/v2/search?query=地铁站&tag=地铁站&region=南山区&output=json&page_num=0&ak=uEt8NGHhXK2pq9kcTSWd6tTAnwafZlS8");
//        HttpGet httpGet = new HttpGet("http://api.map.baidu.com/place/v2/search?query=交通设施,地铁站&location=22.494796260564446,113.92022945914448&radius=2000&output=json&page_num=0&ak=uEt8NGHhXK2pq9kcTSWd6tTAnwafZlS8");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        JSONObject data;
        List<Poi> poiData = new ArrayList<>();
        if (entity != null) {
            data = JSONObject.parseObject(EntityUtils.toString(entity));
            // 解析第0页的数据
            parseData(poiData, data);
            // 解析剩下几页的数据
            int total = Integer.parseInt(data.getString("total"));
            System.out.println(total);
            int totalPage = total / 10;
            for (int i = 1; i < totalPage; i++) {
                Thread.sleep(300);
                httpGet = new HttpGet("http://api.map.baidu.com/place/v2/search?query=地铁站&tag=地铁站&region=南山区&output=json&page_num=" + i + "&ak=uEt8NGHhXK2pq9kcTSWd6tTAnwafZlS8");
//                httpGet = new HttpGet("http://api.map.baidu.com/place/v2/search?query=交通设施,地铁站&location=22.494796260564446,113.92022945914448&radius=2000&output=json&page_num=" + i + "&ak=uEt8NGHhXK2pq9kcTSWd6tTAnwafZlS8");
                response = httpClient.execute(httpGet);
                entity = response.getEntity();
                if (entity != null) {
                    data = JSONObject.parseObject(EntityUtils.toString(entity));
                    System.out.println(i + ":" + data);
                    parseData(poiData, data);
                }
            }
        }

        for (Poi poiDatum : poiData) {
            System.out.println(poiDatum);
        }

        System.out.println(poiData.size());

    }


    /**
     * 从JSON数据中解析出POI数据
     *
     * @param poiData 存放数据的List
     * @param data    待解析的JSON数据
     */
    public static void parseData(List<Poi> poiData, JSONObject data) {
        JSONArray results = data.getJSONArray("results");
        if (results == null) {
            return;
        }
        for (Object result : results) {
            JSONObject jsonResult = (JSONObject) result;
            if (!jsonResult.getString("address").contains("号线")) {
                continue;
            }
            Poi poi = new Poi();
            poi.setName(jsonResult.getString("name"));
            poi.setAddress(jsonResult.getString("address"));
            poi.setArea(jsonResult.getString("area"));
            poi.setCity(jsonResult.getString("city"));
            poi.setProvince(jsonResult.getString("province"));
            poi.setLng(Double.parseDouble(jsonResult.getJSONObject("location").getString("lng")));
            poi.setLat(Double.parseDouble(jsonResult.getJSONObject("location").getString("lat")));
            poiData.add(poi);
        }
    }
}

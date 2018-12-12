package com.daniel.crawler;

import com.daniel.common.CrawlerConstant;
import us.codecraft.webmagic.Spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lingengxiang
 * @date 2018/12/10 18:35
 */
public class Worker {

    public static void main(String[] args) {

        String[] regions = {"福田区", "罗湖区", "南山区", "盐田区", "宝安区", "龙岗区", "龙华区", "坪山区", "光明区"};

        String path = "D:\\深圳市.txt";

        File file = new File("src/main/resources/tag.txt");
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (true) {
                // 一次读一行
                String line = bufferedReader.readLine();
                if (null == line) {
                    break;
                }
                String[] categories = line.split("：");
                if (categories.length > 1) {
                    String[] tags = categories[1].split("、");
                    for (String tag : tags) {
                        for (String region : regions) {
                            Spider.create(new BaiduPoiProcessor())
                                    // 抓取百度地图API
                                    .addUrl(String.format(CrawlerConstant.BAIDU_MAP_API, tag, tag, region, CrawlerConstant.BAIDU_MAP_API_AK))
                                    // 增加数据持久化
                                    .addPipeline(new BaiduPoiPipeline(categories[0], tag, path, region))
                                    // 开启15个线程抓取
                                    .thread(15)
                                    // 启动爬虫
                                    .run();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.nanoTime();
    }
}

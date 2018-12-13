package com.daniel.crawler;

import com.daniel.common.CrawlerConstant;
import us.codecraft.webmagic.Spider;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 应用主程序
 *
 * @author lingengxiang
 * @date 2018/12/10 18:35
 */
public class Worker {

    public static void main(String[] args) {

        // 读取到的抓取区域
//        String[] regions = {"深圳市福田区", "深圳市罗湖区", "深圳市南山区", "深圳市盐田区", "深圳市宝安区", "深圳市龙岗区", "深圳市龙华区", "深圳市坪山区", "深圳市光明区"};
        String[] regions = {"深圳市福田区"};
        // 读取到的保存路径
        String path = "D:\\深圳市-4.txt";

        // 用于存储所有构建的所有tag的URL
        List<String> urls = new LinkedList<>();
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
                    // 通过标签以及抓取区域构建URL
                    for (String region : regions) {
                        for (String tag : tags) {
                            urls.add(String.format(CrawlerConstant.BAIDU_MAP_API, tag, categories[0], region, CrawlerConstant.BAIDU_MAP_API_AK));
                        }
                    }
                }
            }
            LinkedBlockingDeque deque = new LinkedBlockingDeque();
            Spider.create(new BaiduPoiProcessor(deque))
                    // 抓取百度地图API
                    .addUrl(urls.toArray(new String[urls.size()]))
                    // 增加数据持久化
                    //.addPipeline(new BaiduPoiPipeline(path))
                    // 开启15个线程抓取
                    .thread(20)
                    // 启动爬虫
                    .run();

            File storageFile = new File(path);
            // 如果文件不存在，则先创建新文件
            try {
                if (!storageFile.exists()) {
                    storageFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 写入数据
            try (FileWriter fileWriter = new FileWriter(storageFile, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                for (Object data : deque) {
                    printWriter.print(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

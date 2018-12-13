package com.daniel.crawler;

import com.daniel.common.CrawlerConstant;
import com.daniel.common.CrawlerUtil;
import com.daniel.disruptor.DisruptorConsumer;
import com.daniel.disruptor.DisruptorFactory;
import com.daniel.disruptor.DisruptorProducer;
import com.daniel.disruptor.FileData;
import com.daniel.entity.CrawlerRequest;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import us.codecraft.webmagic.Spider;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 应用主程序
 *
 * @author lingengxiang
 * @date 2018/12/10 18:35
 */
public class Worker {

    public static void crawl(CrawlerRequest request) {

        // 用于存储所有构建的所有tag的URL
        List<String> urls = CrawlerUtil.loadUrl(request.getRegions(), request.getCoordType());
        // 创建文件
        File output = CrawlerUtil.createFile(request.getPath());
        // 工厂
        DisruptorFactory factory = new DisruptorFactory();
        // 线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        // 必须为2的幂指数
        int bufferSize = 2048;
        // 初始化Disruptor
        Disruptor<FileData> disruptor = new Disruptor<>(factory,
                bufferSize,
                executor,
                // 多生产者
                ProducerType.MULTI,
                // 默认阻塞策略
                new BlockingWaitStrategy()
        );

        try (FileWriter fileWriter = new FileWriter(output, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // 启动消费者
            disruptor.handleEventsWithWorkerPool(new DisruptorConsumer(printWriter));
            disruptor.start();

            // 启动生产者
            RingBuffer<FileData> ringBuffer = disruptor.getRingBuffer();
            DisruptorProducer producer = new DisruptorProducer(ringBuffer);

            // 启动爬虫
            Spider.create(new BaiduPoiProcessor(producer))
                    // 构建成的URL数组
                    .addUrl(urls.toArray(new String[0]))
                    // 增加数据持久化
                    //.addPipeline(new BaiduPoiPipeline())
                    // 开启20个线程抓取
                    .thread(CrawlerConstant.THREAD_NUM)
                    // 启动爬虫
                    .run();

            // 关闭
            disruptor.shutdown();
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        // 读取到的抓取区域
//        String[] regions = {"深圳市福田区", "深圳市罗湖区", "深圳市南山区", "深圳市盐田区", "深圳市宝安区", "深圳市龙岗区", "深圳市龙华区", "深圳市坪山区", "深圳市光明区"};
        String[] regions = {"深圳市"};
        // 读取到的保存路径
        String path = "D:\\深圳市-2.txt";
        Worker.crawl(new CrawlerRequest(regions, path, "3"));
    }

}

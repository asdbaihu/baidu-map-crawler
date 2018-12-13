package com.daniel.crawler;

import com.daniel.disruptor.DisruptorConsumer;
import com.daniel.disruptor.DisruptorFactory;
import com.daniel.disruptor.DisruptorProducer;
import com.daniel.disruptor.FileData;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.io.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author lingengxiang
 * @date 2018/12/13 11:06
 */
public class TestDisruptorTime {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

//        File file = new File("D:\\test.txt");
//        BlockingDeque deque = new LinkedBlockingDeque();
//        StringBuilder sb = new StringBuilder();
//        try (FileReader fileReader = new FileReader(file);
//             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
//            int index = 0;
//
//            while (true) {
//                // 一次读一行
//                String line = bufferedReader.readLine();
//                if (null == line) {
//                    break;
//                }
//                if (index++ < 20) {
//                    sb.append(line).append("\r\n");
//                }else {
//                    deque.add(sb.toString());
//                    sb.delete(0, sb.length());
//                    index = 0;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        deque.add(sb);

        // 工厂
        DisruptorFactory factory = new DisruptorFactory();
        // 线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        // 必须为2的幂指数
        int BUFFER_SIZE = 16;

        // 初始化Disruptor
        Disruptor<FileData> disruptor = new Disruptor<>(factory,
                BUFFER_SIZE,
                executor,
                // Create a RingBuffer supporting multiple event publishers to the one RingBuffer
                ProducerType.MULTI,
                // 默认阻塞策略
                new BlockingWaitStrategy()
        );

        // 写入数据
        File storageFile = new File("D:\\test-out.txt");
        try {
            if (!storageFile.exists()) {
                storageFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter(storageFile, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            // 启动消费者
            disruptor.handleEventsWithWorkerPool(new DisruptorConsumer(printWriter)
            );
            disruptor.start();

            // 启动生产者
            RingBuffer<FileData> ringBuffer = disruptor.getRingBuffer();
            DisruptorProducer producer = new DisruptorProducer(ringBuffer);
            producer.read("D:\\test.txt");

            // 关闭
            disruptor.shutdown();
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("start:" + start);
        System.out.println("end:" + end);
        System.out.println("消耗时间：" + (end - start));
    }
}

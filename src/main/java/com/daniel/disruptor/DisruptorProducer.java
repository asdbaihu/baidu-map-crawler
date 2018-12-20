package com.daniel.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Disruptor生产者：将抓取下来的数据都放入Disruptor内
 *
 * @author lingengxiang
 * @date 2018/12/13 11:42
 */
public class DisruptorProducer {

    /**
     * 文件读取完毕标记
     */
    private static final String FINISHED = "EOF";

    /**
     * 存储数据的环形Buffer
     */
    private final RingBuffer<FileData> ringBuffer;

    public DisruptorProducer(RingBuffer<FileData> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 存放数据有
     *
     * @param line 一行数据
     */
    public void pushData(String line) {
        long seq = ringBuffer.next();
        try {
            // 获取可用位置
            FileData event = ringBuffer.get(seq);
            // 填充可用位置
            event.setLine(line);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 通知消费者
            ringBuffer.publish(seq);
        }
    }

    /**
     * 读取文件测试
     *
     * @param fileName 文件路径
     */
    public void read(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            StringBuilder sb = new StringBuilder();
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (index++ < 20) {
                    sb.append(line).append("\r\n");
                } else {
                    pushData(sb.toString());
                    sb.delete(0, sb.length());
                    index = 0;
                }
            }
            // 生产数据
            pushData(sb.toString());
            // 结束标志
            pushData(FINISHED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.daniel.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author lingengxiang
 * @date 2018/12/13 11:42
 */
public class DisruptorProducer {
    private static final String FINISHED = "EOF";
    private final RingBuffer<FileData> ringBuffer;

    public DisruptorProducer(RingBuffer<FileData> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

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

    public void read(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            StringBuilder sb = new StringBuilder();
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (index++ < 20) {
                    sb.append(line).append("\r\n");
                }else {
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

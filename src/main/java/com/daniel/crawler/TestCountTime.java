package com.daniel.crawler;

import java.io.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author lingengxiang
 * @date 2018/12/13 11:06
 */
public class TestCountTime {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        File file = new File("D:\\test.txt");
        BlockingDeque deque = new LinkedBlockingDeque();
        StringBuilder sb = new StringBuilder();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            int index = 0;

            while (true) {
                // 一次读一行
                String line = bufferedReader.readLine();
                if (null == line) {
                    break;
                }
                if (index++ < 20) {
                    sb.append(line).append("\r\n");
                }else {
                    deque.add(sb.toString());
                    sb.delete(0, sb.length());
                    index = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deque.add(sb);
        // 如果文件不存在，则先创建新文件
        File storageFile = new File("D:\\test-out.txt");
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

        long end = System.currentTimeMillis();

        System.out.println("start:" + start);
        System.out.println("end:" + end);
        System.out.println("消耗时间：" + (end - start));
    }
}

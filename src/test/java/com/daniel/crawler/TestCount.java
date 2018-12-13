package com.daniel.crawler;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lingengxiang
 * @date 2018/12/11 16:29
 */
public class TestCount {

    public static void main(String[] args) {
        File file = new File("D:\\test.txt");
        long count = 0L;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (true) {
                // 一次读一行
                String line = bufferedReader.readLine();
                if (null == line) {
                    break;
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("总数据量：" + count);
    }
}

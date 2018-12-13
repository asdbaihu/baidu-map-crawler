package com.daniel.disruptor;

import com.lmax.disruptor.WorkHandler;

import java.io.PrintWriter;

/**
 * Disruptor消费者：负责将Disruptor中的数据写入到文件之中
 *
 * @author lingengxiang
 * @date 2018/12/13 11:41
 */
public class DisruptorConsumer implements WorkHandler<FileData> {

    /**
     * 文件读取完毕标记
     */
    private static final String FINISHED = "EOF";

    private PrintWriter printWriter;

    public DisruptorConsumer() {
    }

    public DisruptorConsumer(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }


    @Override
    public void onEvent(FileData event) {
        String line = event.getLine();
        if (line.equals(FINISHED)) {
            return;
        }
        printWriter.print(line);
    }

}


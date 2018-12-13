package com.daniel.disruptor;

import com.lmax.disruptor.WorkHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lingengxiang
 * @date 2018/12/13 11:41
 */
public class DisruptorConsumer implements WorkHandler<FileData> {
    private static final String FINIDHED = "EOF";

    private PrintWriter printWriter;

    public DisruptorConsumer() {
    }

    public DisruptorConsumer(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    @Override
    public void onEvent(FileData event) throws Exception {
        String line = event.getLine();
        if (line.equals(FINIDHED)) {
            return;
        }
        printWriter.print(line);

    }
}


package com.daniel.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author lingengxiang
 * @date 2018/12/13 11:41
 */
public class DisruptorFactory implements EventFactory<FileData> {

    @Override
    public FileData newInstance() {
        return new FileData();
    }
}

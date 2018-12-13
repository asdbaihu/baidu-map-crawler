package com.daniel.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 百度地图POI数据持久化
 *
 * @author lingengxiang
 * @date 2018/12/11 10:22
 */
public class BaiduPoiPipeline implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(BaiduPoiPipeline.class);

    /**
     * 文件存储路径
     */
    private String path;

    public BaiduPoiPipeline() {
    }

    public BaiduPoiPipeline(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 仅负责将数据写入文件之中
     *
     * @param resultItems 结果集
     * @param task        爬虫任务
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
    }
}

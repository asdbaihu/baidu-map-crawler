package com.daniel.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daniel.common.CrawlerConstant;
import com.daniel.common.CrawlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
    public synchronized void process(ResultItems resultItems, Task task) {

        File file = new File(path);
        // 如果文件不存在，则先创建新文件
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        // 写入数据
        try (FileWriter fileWriter = new FileWriter(file, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print(resultItems.get(CrawlerConstant.RESULTS) + "");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

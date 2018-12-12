package com.daniel.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daniel.common.CrawlerConstant;
import com.daniel.common.CrawlerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 百度地图POI数据爬虫
 *
 * @author lingengxiang
 * @date 2018/12/10 18:42
 */
public class BaiduPoiProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(BaiduPoiProcessor.class);

    private LinkedBlockingDeque linkedBlockingDeque;

    /**
     * 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private Site site = Site.me().setRetryTimes(3).setSleepTime(500).setCharset("UTF-8");

    public BaiduPoiProcessor() {
    }

    public BaiduPoiProcessor(LinkedBlockingDeque linkedBlockingDeque) {
        this.linkedBlockingDeque = linkedBlockingDeque;
    }

    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     *
     * @param page 抓取页面
     */
    @Override
    public void process(Page page) {

        if (StringUtils.isBlank(page.getRawText())) {
            logger.error("empty response from:" + page.getUrl());
            return;
        }

        // 将数据转换成JSON格式进行处理
        JSONObject pageData = JSONObject.parseObject(page.getRawText());

        if (pageData.getInteger(CrawlerConstant.STATUS) != 0) {
            logger.error("crawl error from:" + page.getUrl());
            logger.error("reason:{}", pageData.getString("message"));
            return;
        }
        // 抽取数据，并保存下来
        JSONArray results = JSON.parseArray(pageData.getString(CrawlerConstant.RESULTS));
        String topCategory = CrawlerUtil.getUrlParam(page.getUrl().toString(), "tag");
        String subCategory = CrawlerUtil.getUrlParam(page.getUrl().toString(), "query");
        //page.putField(CrawlerConstant.RESULTS, CrawlerUtil.parseData(topCategory, subCategory, results));
        linkedBlockingDeque.add(CrawlerUtil.parseData(topCategory, subCategory, results));

        // 总记录数，如果总记录数大于20条，则进行分页
        int total = Integer.valueOf(pageData.getString(CrawlerConstant.TOTAL));
        String pageNum = CrawlerUtil.getUrlParam(page.getUrl().toString(), "page_num");
        // 如果记录数大于20条，且当前是第一页，则将余下的页数都加入待抓取任务中
        if (total > CrawlerConstant.PAGE_SIZE_NUM && Integer.valueOf(pageNum).equals(0)) {
            int totalPage = CrawlerUtil.calculatePageNum(total, CrawlerConstant.PAGE_SIZE_NUM);
            List<String> restPage = new LinkedList<>();
            int pageIndex = 0;
            while (pageIndex < totalPage) {
                restPage.add(page.getUrl().toString().replace("page_num=0", "page_num=" + ++pageIndex));
            }
            // 将分页后抓取请求加入待抓取任务中
            page.addTargetRequests(restPage);
        }
    }


    @Override
    public Site getSite() {
        return site;
    }

}

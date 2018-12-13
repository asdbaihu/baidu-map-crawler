package com.daniel.common;

/**
 * @author lingengxiang
 * @date 2018/12/11 14:23
 */
public class CrawlerConstant {

    /**
     * 返回状态
     */
    public static final String STATUS = "status";

    /**
     * 返回数量
     */
    public static final String TOTAL = "total";

    /**
     * 返回结果
     */
    public static final String RESULTS = "results";

    /**
     * 百度地图API
     */
    public static final String BAIDU_MAP_API = "http://api.map.baidu.com/place/v2/search?query=%s&tag=%s&region=%s&output=json&coord_type=%s&page_num=0&page_size=20&ak=%s";

    /**
     * 百度地图API的访问token
     */
    public static final String BAIDU_MAP_API_AK = "";

    /**
     * 每页数据条数
     */
    public static final Integer PAGE_SIZE_NUM = 20;

    /**
     * 写入数据格式：一级分类,二级分类,名称,地址,经度,纬度,所属省份,所属城市,所属区县
     */
    public static final String DATA_OUTPUT_FORMAT = "%s,%s,%s,%s,%s,%s,%s,%s,%s";

    /**
     * 爬虫线程数
     */
    public static final Integer THREAD_NUM = 20;

    /**
     * 存放TAG数据的路径
     */
    public static final String TAG_PATH = "src/main/resources/tag.txt";

    /**
     * TAG数据一级分类的分词
     */
    public static final String TAG_TOP_SPLIT = "：";

    /**
     * TAG数据二级分类的分词
     */
    public static final String TAG_SUB_SPLIT = "、";

}

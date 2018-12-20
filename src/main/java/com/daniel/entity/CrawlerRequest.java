package com.daniel.entity;

/**
 * 抓取参数，用来和客户端交互
 *
 * @author lingengxiang
 * @date 2018/12/13 15:56
 */
public class CrawlerRequest {

    /**
     * 抓取地区
     */
    private String[] regions;

    /**
     * 文件存放路径
     */
    private String path;

    /**
     * 坐标类型，1（wgs84ll即GPS经纬度），2（gcj02ll即国测局经纬度坐标），3（bd09ll即百度经纬度坐标），4（bd09mc即百度米制坐标）
     */
    private String coordType;

    /**
     * 验证AK
     */
    private String accessKey;

    public CrawlerRequest() {
    }

    public CrawlerRequest(String[] regions, String path, String coordType, String accessKey) {
        this.regions = regions;
        this.path = path;
        this.coordType = coordType;
        this.accessKey = accessKey;
    }

    public String[] getRegions() {
        return regions;
    }

    public void setRegions(String[] regions) {
        this.regions = regions;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCoordType() {
        return coordType;
    }

    public void setCoordType(String coordType) {
        this.coordType = coordType;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}

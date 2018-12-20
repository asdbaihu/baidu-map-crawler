package com.daniel.entity;


import java.util.List;

/**
 * 区域实体类
 *
 * @author lingengxiang
 * @date 2018/12/18 10:32
 */

public class Area {

    /**
     * 名字
     */
    private String name;

    /**
     * 已解析完毕child
     */
    private List<Area> areaChildList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Area> getAreaChildList() {
        return areaChildList;
    }

    public void setAreaChildList(List<Area> areaChildList) {
        this.areaChildList = areaChildList;
    }

}

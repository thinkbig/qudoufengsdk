package com.qudoufeng.vehiclesdk.sdkpublic.model;

import java.util.List;


/**
 * 趣兜风标签
 */
public class DFTagModel {

    /**
     * 标签名称.
     */
    public String name;
    /**
     * 标签id
     */
    public int tagid;
    /**
     * 上级标签id
     */
    public int parentid;
    /**
     * 上级标签名称
     */
    public String parentname;
    /**
     * 标签图标
     */
    public String icon;
    /**
     * 标签一句话介绍.
     */
    public String shortintro;
    /**
     * 详细介绍.
     */
    public String intro;

    /**
     * 子标签，可能为空.
     */
    public List<DFTagModel> subtags;

}

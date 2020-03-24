package com.qudoufeng.vehiclesdk.sdkpublic.model;


/**
 * 线路景点/导游点模型
 */
public class RoadPointModel {

    /**
     * 语音点id.
     */
    public int id;
    /**
     * 点类型.
     */
    public int type;
    /**
     * The Lat.
     */
    public double lat;
    /**
     * The Lon.
     */
    public double lon;

    /**
     * 语音资源地址.
     */
    public String audiourl;
    /**
     * 语音时长
     */
    public int during;
    /**
     * 资源是否可访问，lock=1，表示资源无访问权限（比如非会员用户），此时audiourl为空.
     */
    public int lock;

    /**
     * 语音名称.
     */
    public String title;
    /**
     * 景点图片.
     */
    public String cover;
    /**
     * 副标题.
     */
    public String subtitle;
    /**
     * 对应的景点id.
     */
    public int sceneid;

}

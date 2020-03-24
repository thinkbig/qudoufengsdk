package com.qudoufeng.vehiclesdk.sdkpublic.model;


/**
 * 趣兜风自驾线路模型
 */
public class RoadTripModel {

    /**
     * 线路里程数，单位米.
     */
    public int mileage;
    /**
     * 推荐游玩季节.
     */
    public String season;

    /**
     * 分组id.
     */
    public int groupid;
    /**
     * 线路id.
     */
    public int wid;

    /**
     * 线路标题.
     */
    public String title;
    /**
     * 线路副标题.
     */
    public String subtitle;

    /**
     * 线路类型. 0=主线，1=支线
     */
    public int type;
    /**
     * 封面.
     */
    public String cover;

    /**
     * 线路介绍.
     */
    public String intro;
    /**
     * 出发城市.
     */
    public String city;

    /**
     * 自驾时长.
     */
    public String duration;


}

package com.qudoufeng.vehiclesdk.model;


import java.io.Serializable;

/**
 * Created by WanYang on 2018/7/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class UserProfile implements Serializable {
    public String userid;
    public String name = "";
    public String email = "";
    public String phone = "";
    public String intro = "";
    public String avatar = "";
    public String iconid = "";
    public int gender;
    public String remarkName = "";//好友备注
    public String resid = "";//用户中心背景图
    public int bought;
    public String dob = "";
    public String city = "";
    public double lon;
    public double lat;
    public int member;
    public int admin;
    public int holder;
    public String head;

    public boolean lastone = false;

}

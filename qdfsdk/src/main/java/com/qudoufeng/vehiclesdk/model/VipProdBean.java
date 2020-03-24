package com.qudoufeng.vehiclesdk.model;

import java.io.Serializable;

/**
 * Created by WanYang on 2019/1/18/018.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class VipProdBean implements Serializable, Cloneable {
    public int type;
    public String title;
    public float price;
    public String waycntstr;
    public String duringstr;
    public String hint;
    public int prodid;
    public String iosprodid;
    public int countable;
    public int hascoupon;
    public int count;
    public String couponid;//输入的邀请码
    public String detailid;
    public String code;

    public boolean isChecked;

    @Override
    public VipProdBean clone() {
        VipProdBean obj = null;
        //调用Object类的clone方法，返回一个Object实例
        try {
            obj = (VipProdBean) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}

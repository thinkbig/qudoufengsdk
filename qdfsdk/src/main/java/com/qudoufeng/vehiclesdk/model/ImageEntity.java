package com.qudoufeng.vehiclesdk.model;

import java.io.Serializable;

/**
 * Created by WanYang on 2018/7/16/016.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class ImageEntity implements Serializable {
    public String resid;
    public String info;
    public int reloadcnt;//重新加载次数
    public boolean isEmpty;
    public int placeholder;
}

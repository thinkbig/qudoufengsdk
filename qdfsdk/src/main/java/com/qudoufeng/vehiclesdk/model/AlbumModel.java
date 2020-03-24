package com.qudoufeng.vehiclesdk.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WanYang on 2019/4/3/003.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class AlbumModel implements Serializable {
    public String title;
    public String subtitle;
    public int waycnt;
    public int target;
    public int prodid;
    public String params;
    public String intro;
    public String tagicon;
    public String coverresid;
    public String tripthumb;
    public int bought;

    public List<GetGpsRouteModel> waylist;

    public boolean isCheck;
}

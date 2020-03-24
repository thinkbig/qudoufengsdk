package com.qudoufeng.vehiclesdk.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.utils.StringUtils;
import com.qudoufeng.vehiclesdk.utils.encode.AES;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WanYang on 2018/7/10/010.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class GpsRouteResp implements Serializable {
    public List<GetGpsRouteModel> pointlist;
    public List<GetGpsRouteModel> scenelist;

    public int bought;
    public long expiretime;
    public String audiobegin;
    public String audiobegin2;
    public int beginduring;
    public int beginduring2;
    public int wayid;
    public int reportthres;
    public int begingap;  // 片头播放间隔  默认12小时

    public String i;
    public String l;

    public List<GetGpsRouteModel> radios;   // 景点节目单
    public List<GetGpsRouteModel> arearadios;   // 景点所属景区节目单

    public boolean isActived() {
        return expiretime > System.currentTimeMillis();
    }


    public boolean parsePoints() {
        if ((null == pointlist || pointlist.size() == 0) && !StringUtils.isNullOrEmpty(i) && !StringUtils.isNullOrEmpty(l)) {
            String decString = AES.decrDFStr(l, i, true);
            if (StringUtils.isNullOrEmpty(decString)) {
                return false;
            }
            pointlist = new Gson().fromJson(decString, new TypeToken<List<GetGpsRouteModel>>() {}.getType());
        }
        return true;
    }

    public boolean parseRadio() {
        if ((null == radios || radios.size() == 0) && !StringUtils.isNullOrEmpty(i) && !StringUtils.isNullOrEmpty(l)) {
            String decString = AES.decrDFStr(l, i, true);
            if (StringUtils.isNullOrEmpty(decString)) {
                return false;
            }
            radios = new Gson().fromJson(decString, new TypeToken<List<GetGpsRouteModel>>() {}.getType());
        }
        return true;
    }
}

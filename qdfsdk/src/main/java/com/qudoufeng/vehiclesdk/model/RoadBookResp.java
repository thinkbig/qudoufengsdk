package com.qudoufeng.vehiclesdk.model;

import com.google.gson.Gson;
import com.qiniu.android.utils.StringUtils;
import com.qudoufeng.vehiclesdk.utils.encode.AES;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WanYang on 2018/7/10/010.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class RoadBookResp implements Serializable {
    public List<RoadBookModel> waylist;
    public RoadBookModel way;

    public int typeid;
    public List<AlbumModel> albums;
    public AlbumModel album;


    public String i;
    public String l;

    public void parseWay() {
        if (null == way && !StringUtils.isNullOrEmpty(i) && !StringUtils.isNullOrEmpty(l)) {
            String decString = AES.decrDFStr(l, i, false);
            if (!StringUtils.isNullOrEmpty(decString)) {
                way = new Gson().fromJson(decString, RoadBookModel.class);
            }
        }
    }
}

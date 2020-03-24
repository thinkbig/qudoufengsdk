package com.qudoufeng.vehiclesdk.model.random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.utils.StringUtils;
import com.qudoufeng.vehiclesdk.utils.encode.AES;

import java.util.List;

/**
 * Created by WanYang on 2019/5/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class MusicResp {
    public List<MusicBean> resources;
    public List<MusicBean> radios;

    public String i;
    public String l;

    public boolean parseRadio() {
        if ((null == radios || radios.size() == 0) && !StringUtils.isNullOrEmpty(i) && !StringUtils.isNullOrEmpty(l)) {
            String decString = AES.decrDFStr(l, i, true);
            if (StringUtils.isNullOrEmpty(decString)) {
                return false;
            }
            radios = new Gson().fromJson(decString, new TypeToken<List<MusicBean>>() {}.getType());
        }
        return true;
    }

    public boolean parseRes() {
        if ((null == resources || resources.size() == 0) && !StringUtils.isNullOrEmpty(i) && !StringUtils.isNullOrEmpty(l)) {
            String decString = AES.decrDFStr(l, i, true);
            if (StringUtils.isNullOrEmpty(decString)) {
                return false;
            }
            resources = new Gson().fromJson(decString, new TypeToken<List<MusicBean>>() {}.getType());
        }
        return true;
    }
}

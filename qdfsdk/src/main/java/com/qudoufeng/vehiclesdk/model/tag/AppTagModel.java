package com.qudoufeng.vehiclesdk.model.tag;

import com.qudoufeng.vehiclesdk.sdkpublic.model.DFTagModel;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.Utils;

import java.io.Serializable;

/**
 * Created by WanYang on 2019/1/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class AppTagModel implements Serializable {
    public int valid;
    public String parentname;
    public String name;
    public int tagid;
    public int parentid;
    public int rank;
    public String icon;
    public String shortintro;
    public String intro;

    // 转换为api对外数据
    public DFTagModel toExportModel() {
        DFTagModel model = new DFTagModel();
        model.name = name;
        model.tagid = tagid;
        model.parentid = parentid;
        model.parentname = parentname;
        model.icon = Utils.ctUrl(icon, AppConstants.Width_1080);
        model.shortintro = shortintro;
        model.intro = intro;
        return model;
    }

}

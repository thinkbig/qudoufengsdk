package com.qudoufeng.vehiclesdk.model.tag;

import java.io.Serializable;
import java.util.List;

;

/**
 * Created by WanYang on 2019/1/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class AppTagResp implements Serializable {
    public List<AppTagModel> taglist;
    public String tagname;
    public boolean isChecked;
    public int parentid;
}

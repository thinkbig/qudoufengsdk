package com.qudoufeng.vehiclesdk.ui.view;

import com.qudoufeng.vehiclesdk.base.IBaseView;
import com.qudoufeng.vehiclesdk.model.tag.AppTagResp;

/**
 * Created by WanYang on 2019/1/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public interface TagViewI extends IBaseView {
    void loadHomeTags(AppTagResp o);
    void loadAllTags(AppTagResp resp);
}

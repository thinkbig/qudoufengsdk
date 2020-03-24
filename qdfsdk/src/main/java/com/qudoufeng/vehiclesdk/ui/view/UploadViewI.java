package com.qudoufeng.vehiclesdk.ui.view;

import com.qudoufeng.vehiclesdk.base.IBaseView;

/**
 * Created by WanYang on 2018/7/27/027.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public interface UploadViewI extends IBaseView {
    void onUploadSuccess(String resid);
    void onUploadError(String error);
}

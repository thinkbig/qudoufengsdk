package com.qudoufeng.vehiclesdk.model.random;

import android.text.TextUtils;

/**
 * Created by WanYang on 2019/5/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class MusicBean {
    public String title;
    public String summary;
    public String resid;
    public int during;
    public int playstat;// 播放状态 0表示未播放 1表示正在播放 2表示播放完成

    public MusicBean() {
    }

    public MusicBean(String resid) {
        this.resid = resid;
    }

    public String resName() {
        if (TextUtils.isEmpty(title)) {
            return summary;
        }
        return title;
    }
}

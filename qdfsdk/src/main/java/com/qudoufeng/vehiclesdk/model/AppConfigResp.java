package com.qudoufeng.vehiclesdk.model;

import android.os.SystemClock;

import java.io.Serializable;

/**
 * Created by WanYang on 2018/11/30/030.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class AppConfigResp implements Serializable {
    public Version version;
    public String support;

    public long servtime;
    public long elapsedRealtime;    // 获得servtime时，开机启动时长

    // 获取服务器时间
    public long serviceTime() {
        if (servtime > 0 && elapsedRealtime > 0) {
            return SystemClock.elapsedRealtime() - elapsedRealtime + servtime;
        }
        return System.currentTimeMillis();
    }
}

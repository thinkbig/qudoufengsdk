package com.qudoufeng.vehiclesdk.model;

import android.text.TextUtils;

import com.qudoufeng.vehiclesdk.http.NetworkConstants;
import com.qudoufeng.vehiclesdk.utils.filelogger.FL;
import com.qudoufeng.vehiclesdk.utils.filelogger.FLGps;

import java.io.Serializable;

/**
 * Created by WanYang on 2018/11/30/030.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class Version implements Serializable {
    public int force;//1表示强制更新
    public String msg; // 更新内容
    public String url;//下载地址
    public String version;

    // log开关配置
    public int loglevel = -1;    // -1 表示关闭
    public int logreport = 0;   // 是否上报log
    // gps log 开关配置
    public int gpslevel = -1;    // -1 表示关闭
    public int gpsreport = 0;   // 是否上报log
    public int instgpsRepot = 0;    // 是否实时上报gps

    // 服务器URL配置
    public String fileurl;      // 下载地址
    public String streamurl;    // 流媒体地址
    public String backupurl;    // 万一域名挂了，这是备用域名（或ip直连）

    // 音频相关
    public String audiobegin2;          // 片头
    public int audiobeginduring2;    // 片头长度

    // 使用该配置
    public void applyConfig() {
        if (loglevel < 0) {
            FL.setEnableFileLog(false);
        } else {
            FL.setEnableFileLog(true);
        }
        if (gpslevel < 0) {
            FLGps.setEnabled(false);
        } else {
            FLGps.setEnabled(true);
        }
        // 修改地址
        if (!TextUtils.isEmpty(fileurl) && fileurl.startsWith("http")) {
            NetworkConstants.HOST_FILE_URL = fileurl;
        }
        if (!TextUtils.isEmpty(streamurl) && streamurl.startsWith("http")) {
            NetworkConstants.HOST_AUDIO_URL = fileurl;
        }
    }
}

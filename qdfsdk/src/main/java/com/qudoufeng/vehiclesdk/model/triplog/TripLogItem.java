package com.qudoufeng.vehiclesdk.model.triplog;


import com.qudoufeng.vehiclesdk.model.GetGpsRouteModel;
import com.qudoufeng.vehiclesdk.utils.AppConstants;

import java.io.Serializable;

/**
 * Created by WanYang on 2018/11/5/005.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class TripLogItem implements Serializable {

    // log事件
    public static int eAudioLogTypeBegin = 0;
    public static int eAudioLogTypeIntro = 1;
    public static int eAudioLogTypeScene = 2;       // 景点语音
    public static int eAudioLogTypeParking = 3;     // 废弃
    public static int eAudioLogTypeAudio = 4;
    public static int eAudioLogTypeEnd = 5;
    public static int eAudioLogTypeYaw = 6;         // 废弃
    public static int eAudioLogTypeScenePre = 7;    // 废弃
    public static int eAudioLogTypeRandGps = 8;
    public static int eAudioLogTypeRandCity = 9;
    public static int eAudioLogTypeRadio = 10;  // 节目单
    public static int eAudioLogTypeMusic = 11;  // 音乐

    public static int eAudioLogTypeUnknow = 99999;


    // 音频事件
    public static int eAudioLogStatNone = 0;
    public static int eAudioLogStatQuene = 1;   // 加入播放列表
    public static int eAudioLogStatBegin = 2;   // 开始播放
    public static int eAudioLogStatFinish = 3;  // 播放正常结束
    public static int eAudioLogStatFail = 4;    // 播放失败
    public static int eAudioLogStatReplace = 5; // 播放被替换


    public TripLogItem(int itemtype, String resid){
        this.resid = resid;
        this.logtype = itemtype;
    }

    public TripLogItem(GetGpsRouteModel item) {
        this.id = item.id;
        this.sceneid = item.sceneid;
        this.type = item.type;
        this.lat = item.lat;
        this.lon = item.lon;
        this.title = item.title;
        this.resid = item.resid;
        if (item.isScenept()) {
            this.logtype = eAudioLogTypeScene;
        } else if (item.isAudiopt()) {
            this.logtype = eAudioLogTypeAudio;
        } else if (item.isParkingPt()) {
            this.logtype = eAudioLogTypeParking;
        }
        this.arrivetime = System.currentTimeMillis() / 1000;
    }

    public int id;
    public int sceneid;    // 所属景点id
    public String title;

    public int type;   // 播报点 10, 大景点  1, 小景点 2,  type=9 途经点  type = 16 停车点
    public String resid;

    public double lat;
    public double lon;

    public int logtype;
    public int status;

    public long arrivetime; // 发生时间


    public boolean isAudiopt() {
        return this.type == AppConstants.RoadBookVoicePoint;
    }

    public boolean isScenept() {
        return this.type == AppConstants.RoadBookBigPoint || this.type == AppConstants.RoadBookSmallPoint;
    }

    public long arriveDate() {
        return System.currentTimeMillis() / 1000;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public String typeString(){
        if (eAudioLogTypeScene == logtype){
            return "后";
        }else if (eAudioLogTypeScenePre == logtype){
            return "前";
        }else if (eAudioLogTypeParking == logtype){
            return "停";
        }else if (eAudioLogTypeYaw == logtype){
            return "偏";
        }else if (eAudioLogTypeAudio == logtype){
            return "音";
        }else if (eAudioLogTypeBegin == logtype || eAudioLogTypeEnd == logtype){
            return "后";
        }
        return "无";
    }

    public String statString(){
        if (eAudioLogStatQuene == status) {
            return "加入列表";
        } else if (eAudioLogStatBegin == status) {
            return "播放开始";
        } else if (eAudioLogStatFinish == status) {
            return "播放完成";
        } else if (eAudioLogStatFail == status) {
            return "播放失败";
        } else if (eAudioLogStatReplace == status) {
            return "被替换";
        }
        return "无";
    }

}

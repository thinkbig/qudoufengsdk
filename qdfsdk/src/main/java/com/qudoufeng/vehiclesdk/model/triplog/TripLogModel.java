package com.qudoufeng.vehiclesdk.model.triplog;

import android.text.TextUtils;

import com.qudoufeng.vehiclesdk.model.RoadBookModel;
import com.qudoufeng.vehiclesdk.utils.Utils;

import java.io.Serializable;
import java.util.List;


/**
 * Created by WanYang on 2018/11/5/005.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class TripLogModel implements Serializable {

    public TripLogModel(RoadBookModel trip, String userid, int firstItem) {
        this.wid = trip.id;
        this.gid = trip.gid;
        this.userid = userid;
        this.title = trip.title;
        this.firstitem = firstItem;
        this.starttime = System.currentTimeMillis();
    }

    public TripLogModel(String userid, String title,int wid) {
        this.wid = wid;
        this.userid = userid;
        this.title = title;
    }

    public boolean isEmulator;
    public int wid;
    public int gid;
    public String userid;
    public String title;
    public long starttime;
    // 不为空表示行程结束（到达终点，或者手动确认结束）
    public long endtime;

    public String filename;

    // 用户最后一次更新的位置
    public double lat;
    public double lon;

    public boolean isUpload;//是否上传过

    public String gpslogids;
    public List<String> gpslogfile;

    // 表示用户最近一次选择的出发点，用于继续行程
    public int firstitem;
    public int isBegin; // 是否行程开始

    // 实际播放的语音列表
    public List<TripLogItem> playitems;

    // 实际经过的大小景点、语音点
    public List<TripLogItem> meetitems;


    public TripLogItem lastPlayeItem(String resid) {
        TripLogItem item = null;
        if (null != playitems) {
            String playerResid = Utils.getPlayerResid(resid);
            for (TripLogItem model : playitems) {
                if (TextUtils.equals(playerResid, Utils.getPlayerResid(model.resid))) {
                    item = model;
                    break;
                }
            }
        }
        return item;
    }

    public boolean isTripBegin() {
        if (isBegin == 0 && meetitems != null && meetitems.size() > 0) {
            isBegin = 1;
        }
        return isBegin > 0;
    }
}

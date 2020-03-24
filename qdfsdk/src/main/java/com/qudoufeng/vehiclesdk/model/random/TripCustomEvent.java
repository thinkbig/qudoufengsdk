package com.qudoufeng.vehiclesdk.model.random;

import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.qudoufeng.vehiclesdk.DFSdk;
import com.qudoufeng.vehiclesdk.model.GetGpsRouteModel;
import com.qudoufeng.vehiclesdk.utils.Utils;

import java.io.File;

/**
 * Created by WanYang on 2019/5/6/006.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class TripCustomEvent {
    public int type;  // 0纯导游 1导航+导游
    public String logid; // 行程唯一id userid_Systerm.currentTime
    public String userid;
    public int wayid;
    public String gps; // lon,lat,海拔
    public String gpsinfo; // wifi,卫星数
    public int stat;//0=无，1=前台，2=后台
    public String gpslogs;

    // 0=gps(这时mpid只表示最近点),1=给gps权限，2=行程开始，3=行程结束,4=轨迹记录开始，5=轨迹记录停止，6=轨迹记录信息
    // 10=语音加入队列，11=语音开始播放，12=播放结束，13=播放失败，
    // 20=继续播放，21=用户暂停，22=短暂停（电话，外部导航等），23=qq音乐等事件
    // 30=试用领取，31=试用赠送获得，32=下单，33=支付成功
    public int event;

    public int mpid;
    public float mpdist;
    public String resid;
    public int source; // -1=没有源如未购，2=网络源，1=本地音频， 10表示网络音乐 11表示本地音乐
    public String addi;
    public long createtime;

    public TripCustomEvent(int event, String userid) {
        this.event = event;
        this.createtime = System.currentTimeMillis();
        this.userid = userid;
        this.stat = DFSdk.appRunningStat();
    }

    public TripCustomEvent(String userid) {
        this.createtime = System.currentTimeMillis();
        this.userid = userid;
        this.stat = DFSdk.appRunningStat();
    }

    public void updateWithLoc(Location location, NetworkInfo netInfo) {
        String net = "未知";
        if (netInfo != null) {
            if (netInfo.isConnected()) {
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    net = "WIFI";
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    net = "MOBILE";
                }
            }
        }
        if (location != null) {
            this.gps = String.format("%.5f,%.5f,%d", location.getLongitude(), location.getLatitude(), (int) location.getAltitude());
            this.gpsinfo = String.format("%f,%f,%f;%s", location.getAltitude(), location.getSpeed(), location.getBearing(), net);
        }
    }


    public void updateWithItem(GetGpsRouteModel item) {
        try {
            if (item != null) {
                this.mpid = item.id;
                this.resid = item.resid;
                if (item.lock == 1 && TextUtils.isEmpty(item.resid)) {
                    this.source = -1;
                } else {
                    String path = Utils.getLocalCachePath(resid);
                    File file = new File(path);
                    if (file.exists() && file.length() > 0) {
                        this.source = 1;
                    } else {
                        this.source = 2;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String geneLogid() {
        return userid + "_" + System.currentTimeMillis();
    }
}

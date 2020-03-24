package com.qudoufeng.vehiclesdk.model;

import android.text.TextUtils;

import com.qudoufeng.vehiclesdk.sdkpublic.model.RoadPointModel;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WanYang on 2018/7/10/010.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class GetGpsRouteModel implements Serializable {
    public int type;
    public int subtype;     // 1=普通lbs点，2=必经之路语音点（废弃），3=次级LBS（冲突时等待），4=备用LBS（冲突是丢弃），11=节目触发点
    public double lat;
    public double lon;
    public double glat;
    public double glon;
    public float heading;// 方向
    public int headflag;    // 方向判断阈值，小于10表示无需判断方向
    public String resid;
    public int during;// 语音时长
    public String comment;
    public int id;
    public String title;
    public int checked;
    public int rate;
    public int parentid;
    public String pic;
    public String size;
    public String subtitle;
    public int sceneid;
    public String priceinfo;
    public int playduring;
    public int cityid;
    public int delay;
    public float calspeed;
    public int preduring;
    public int predelay;
    public String preresid;
    public int parkid;

    public String name;
    public String readname;
    public int lock;//1表示锁，其他表示
    public int lockpre;
    public int wayid;

    public boolean isnearest;//是否是最近点
    public float distance;
    public String bigIndex;//大点的位置
    public boolean isplaying;//语音点是否正在播放
    public int offset;
    public String address;
    public int flag;
    public String cover;
    public String duration;
    public String mapicon;
    public float altitude;

    public GetGpsRouteModel park;

    public int viewType;
    public boolean canclick = true;
    public int iconType; // 1表示显示索引 0表示显示图片
    public String city;
    public long noneRepeatDuring;// 可重复播放时间
    public int isQuene;//1=已经加入播放队列， 2=播放完毕
    public int downstat; //下载状态  -1下载失败 1下载成功 0未下载
    public String cacheresid;//缓存的本地地址
    public AlbumModel albumModel;

    public long endtime;
    public int playcnt;

    // rpid > 0 表示这是一个节目
    public int rpid;


    public GetGpsRouteModel() {
    }

    public GetGpsRouteModel(String resid, String title) {
        this.resid = resid;
        this.title = title;
        this.name = title;
    }

    public boolean isNormalGpsPt() {
        return subtype < 2;
    }

    // 同一个scene，lbs点=1，必播点=2，触发点=10
    public String uniSceneKey() {
        if (isRadioAudio()) {
            return String.format("%d_%s", sceneid, null != resid ? resid : "");
        }
        int sub = subtype == 0 ? 1 : subtype;   // 0当场是1
        return String.format("%d_%d", sceneid, sub);
    }

    public boolean isGpsTrigger() {
        return subtype == 11 && sceneid > 0;
    }

    public boolean isRadioAudio() {
        return rpid > 0;
    }

    /**
     * 是否是大小点
     *
     * @return
     */
    public boolean isScenept() {
        return type == AppConstants.RoadBookBigPoint || type == AppConstants.RoadBookSmallPoint;
    }

    public boolean isSmallPoint() {
        return type == AppConstants.RoadBookSmallPoint;
    }

    public boolean isBigPoint() {
        return type == AppConstants.RoadBookBigPoint;
    }

    public boolean isParkingPt() {
        return type == AppConstants.RoadBookParkPoint;
    }

    public boolean isAudiopt() {
        return type == AppConstants.RoadBookVoicePoint || type == AppConstants.RoadBookVoicePointLong;
    }

    public boolean hasAudio() {
        return !TextUtils.isEmpty(resid) && resid.length() > 5;
    }

    public boolean isRandomPoint() {
        return type >= 20 && type <= 22;
    }

    public boolean isRandomGpsWayPt() {
        return type == 22 || type == 10;
    }

    public boolean isShop() {
        return type == 3;
    }

    public boolean isHotel() {
        return isShop() && subtype == 1;
    }

    public boolean isRestaurant() {
        return isShop() && subtype == 2;
    }

    public String getShowTitle() {
        return TextUtils.isEmpty(name) ? title : name;
    }

    // 冲突时加入播放等待列表
    public boolean isWaitingAudio() {
        return subtype == 3;
    }

    // 冲突时加入丢弃
    public boolean isDropAudio() {
        return subtype == 4;
    }

    /**
     * 是否是途经点
     *
     * @return
     */
    public boolean isWayPoint() {
        return type == AppConstants.RoadBookWayPoint;
    }

    public int audioPlayDist(float tolDelay) {
        float speed = Math.max(10, calspeed) / 3.6f;
        float delay1 = Math.min(Math.max(delay, tolDelay), during);
        return (int) (speed * Math.max(10, during + delay1)) + 100;
    }

    public boolean isAudioDefault() {
        return type == 10 || isScenept();
    }

    public List<ImageEntity> getItemPic() {
        List<ImageEntity> list = new ArrayList<>();
        if (!TextUtils.isEmpty(pic)) {
            String[] pics = pic.split(",");
            for (String resid : pics) {
                ImageEntity entity = new ImageEntity();
                entity.resid = resid;
                list.add(entity);
            }
        }
        return list;
    }

    public String getTitle() {
        return TextUtils.isEmpty(name) ? TextUtils.isEmpty(title) ? "" : title : name;
    }

    public boolean isNoneGpsPoint() {
        return type == 20;
    }

    // 转换为api对外数据
    public RoadPointModel toExportModel() {
        RoadPointModel model = new RoadPointModel();
        model.type = type;
        model.lat = lat;
        model.lon = lon;
        model.audiourl = Utils.getPlayerUrl(resid);
        model.during = during;
        model.lock = lock;
        model.id = id;
        model.title = title;

        model.subtitle = subtitle;
        model.sceneid = sceneid;

        if (!TextUtils.isEmpty(pic)) {
            String[] pics = pic.split(",");
            if (pics.length > 0) {
                model.cover = pics[0];
            }
        }
        return model;
    }

    @Override
    public String toString() {
        return "GetGpsRouteModel{" +
                "type=" + type +
                ", subtype=" + subtype +
                ", lat=" + lat +
                ", lon=" + lon +
                ", heading=" + heading +
                ", resid='" + resid + '\'' +
                ", during=" + during +
                ", comment='" + comment + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", checked=" + checked +
                ", rate=" + rate +
                ", parentid=" + parentid +
                ", pic='" + pic + '\'' +
                ", size='" + size + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", sceneid=" + sceneid +
                ", priceinfo='" + priceinfo + '\'' +
                ", playduring=" + playduring +
                ", cityid=" + cityid +
                ", delay=" + delay +
                ", calspeed=" + calspeed +
                ", preduring=" + preduring +
                ", predelay=" + predelay +
                ", preresid='" + preresid + '\'' +
                ", parkid=" + parkid +
                ", name='" + name + '\'' +
                ", readname='" + readname + '\'' +
                ", lock=" + lock +
                ", lockpre=" + lockpre +
                ", isnearest=" + isnearest +
                ", distance=" + distance +
                ", bigIndex='" + bigIndex + '\'' +
                ", isplaying=" + isplaying +
                ", offset=" + offset +
                ", address='" + address + '\'' +
                ", park=" + park +
                ", viewType=" + viewType +
                ", canclick=" + canclick +
                ", iconType=" + iconType +
                ", city='" + city + '\'' +
                '}';
    }
}
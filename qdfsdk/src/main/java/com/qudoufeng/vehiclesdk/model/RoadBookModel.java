package com.qudoufeng.vehiclesdk.model;

import com.qudoufeng.vehiclesdk.sdkpublic.model.RoadTripModel;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WanYang on 2018/7/10/010.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class RoadBookModel implements Serializable {
    public int mileage;
    public String season;
    public int subtype;//1表示环线
    public double firstlon;
    public double firstlat;
    public double lastlat;
    public double lastlon;
    public int pointcnt;
    public String first;
    public String last;
    public String introsound;
    public String audiobegin;
    public String audiobegin2;
    public String audioend;
    public String audiopark;
    public String audioyaw;

    public int groupid;
    public int wayid;

    public int id;
    public String title;
    public String subtitle;
    public double lat;
    public double lon;
    public int type;
    public int naviflag;
    public String resid;
    public String name;
    public String cover;
    public int waycnt;
    public String cover2;
    public String intro;
    public String city;
    public int playcnt;
    public String size;
    public int piccnt;
    public float price;
    public int salecnt;
    public int likecnt;
    public boolean isplaying;
    public String duration;
    public String tripthumb;
    public String tripthumb2;
    public int bought;
    public String groupname;
    public String features;
    public String pic;
    public String address;
    public String opentime;
    public int playduring;
    public String priceinfo;
    public int gid;
    public int hotelcnt;// 线路中酒店的个数
    public int rescnt;//线路中饭店的个数
    public int parentid;
    public int cityid;

    public int audiocnt;
    public String sharehint;

    public long expiretime;
    public long createtime;
    public int wid;
    public String userid;
    public int status;//
    public String buyhint;

    public int typeid;
    public int viewType;

    public boolean isfav;

    public boolean isChecked;

    public boolean showNavi;
    public float altitude;

    public int prodid;
    public String tagicon;
    public int beginduring;
    public int beginduring2;
    public int introduring;

    public List<AlbumModel> albums;

    /**
     * 是否是城市观光游
     *
     * @return
     */
    public boolean isSightSeeingTrip() {
        return type == 2;
    }

    /**
     * 是否支持导航
     *
     * @return
     */
    public boolean isNaviable() {
        return naviflag == 0 || naviflag == 1;
    }

    /**
     * 是否购买
     * @return
     */
    public boolean isBought() {
        return bought == 1 && isValid();
    }

    /**
     * 是否有效
     * @return
     */
    public boolean isValid(){
        return expiretime > System.currentTimeMillis();
    }

    public boolean isAlbum() {
        return albums != null && albums.size() > 0;
    }

    // 转换为api对外数据
    public RoadTripModel toExportModel() {
        RoadTripModel model = new RoadTripModel();
        model.mileage = mileage;
        model.season = season;
        model.groupid = groupid;
        model.wid = wid;
        model.title = title;
        model.subtitle = subtitle;
        model.type = type;
        model.cover = Utils.ctUrl(cover, AppConstants.Width_1080);
        model.intro = intro;
        model.city = city;
        model.duration = duration;
        return model;
    }
}

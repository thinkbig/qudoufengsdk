package com.qudoufeng.vehiclesdk.model;

import com.google.gson.Gson;
import com.qudoufeng.vehiclesdk.utils.LocationPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WanYang on 2018/12/19/019.
 * 模拟导航数据
 * company:杭州湾心网络科技有限公司
 */

public class SimuNavicfgResp implements Serializable {

    public String simucfg;

    static public class SimuNavicfg implements Serializable {
        public float speed;
        public float timediff;
        public List<String> gpslist;

        public List<LocationPoint> parseLocations() {
            List<LocationPoint> tmplist = new ArrayList<>();
            if (null != gpslist) {
                for (String gps : gpslist) {
                    String[] lines = gps.split(",");
                    LocationPoint location = new LocationPoint();
                    location.lon = Double.parseDouble(lines[0]);
                    location.lat = Double.parseDouble(lines[1]);
                    location.bearing = Float.parseFloat(lines[2]);
                    location.speed = speed;
                    tmplist.add(location);
                }
            }
            return tmplist;
        }
    }

    public SimuNavicfg parseCfg() {
        try {
            return new Gson().fromJson(simucfg, SimuNavicfg.class);
        } catch (Exception e) {
        }
        return null;
    }
}



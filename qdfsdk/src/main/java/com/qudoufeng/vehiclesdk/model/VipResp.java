package com.qudoufeng.vehiclesdk.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WanYang on 2019/1/18/018.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class VipResp implements Serializable {
    public List<VipProdBean> prodlist;
    public VipInfo vipinfo;
    public List<AlbumModel> albums;

    public boolean vipUser(){
        return vipinfo != null && (vipinfo.isVipYear() || (vipinfo.isVip() && albums != null && albums.size() > 0));
    }

    public boolean albumUser(){
        return albums != null && albums.size() > 0;
    }

    public class VipInfo implements Serializable{
        public int type;
        public long expiretime;
        public int role;

        public boolean isVip(){
            return  type >= 10 && expiretime > System.currentTimeMillis();
        }

        public boolean isFreeTryUser(){
            return role == 2;
        }

        public boolean isVipYear(){
            return type >= 20 && expiretime > System.currentTimeMillis();
        }

        public String getVipType(){
            if (type == 20){
                return "年会员";
            } else if (type == 10){
                return "普通会员";
            }
            return "";
        }
    }
}

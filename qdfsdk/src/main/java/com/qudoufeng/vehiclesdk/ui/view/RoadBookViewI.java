package com.qudoufeng.vehiclesdk.ui.view;


import com.qudoufeng.vehiclesdk.base.IBaseView;
import com.qudoufeng.vehiclesdk.model.GpsRouteResp;
import com.qudoufeng.vehiclesdk.model.RoadBookResp;

/**
 * Created by WanYang on 2018/7/10/010.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public interface RoadBookViewI extends IBaseView {
    void loadWayList(RoadBookResp resp);

    void loadDetail(RoadBookResp resp);

    void loadViewPoints(GpsRouteResp resp);
}

package com.qudoufeng.vehiclesdk.ui.view;

import com.qudoufeng.vehiclesdk.base.IBaseView;
import com.qudoufeng.vehiclesdk.model.GpsRouteResp;
import com.qudoufeng.vehiclesdk.model.SimuNavicfgResp;

/**
 * Created by WanYang on 2019/8/20/020.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public interface PtAroundViewI extends IBaseView {

    void loadPtAroundPoints(GpsRouteResp resp);
    void loadDestinationList(GpsRouteResp resp);
    void loadSceneAudios(GpsRouteResp resp);
    void loadSimuConfig(SimuNavicfgResp resp);

    // event=(ptarrount, destlist, sceneaudio, simucfg)
    void loadPtFailed(String msg, String event);
}

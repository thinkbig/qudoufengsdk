package com.qudoufeng.vehiclesdk.ui.presenter;

import com.qudoufeng.vehiclesdk.base.IBasePresenter;
import com.qudoufeng.vehiclesdk.http.ErrorHandler;
import com.qudoufeng.vehiclesdk.http.HttpService;
import com.qudoufeng.vehiclesdk.http.subscribers.EmptySubscriber;
import com.qudoufeng.vehiclesdk.http.subscribers.SubscriberListener;
import com.qudoufeng.vehiclesdk.model.GpsRouteResp;
import com.qudoufeng.vehiclesdk.model.SimuNavicfgResp;
import com.qudoufeng.vehiclesdk.ui.view.PtAroundViewI;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.LocationPoint;

/**
 * Created by WanYang on 2019/8/20/020.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class PtAroundPresenter extends IBasePresenter<PtAroundViewI> {

    public PtAroundPresenter(PtAroundViewI view) {
        attech(view);
    }

    public void getPtAroundPoints(LocationPoint location) {
        api().loadAroundPoints(wrapper().getUserId(), location)
                .map(new HttpService.HttpResultMap<GpsRouteResp>())
                .compose(new HttpService.CommonScheduler<GpsRouteResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<GpsRouteResp>() {
                    @Override
                    public void onSuccess(GpsRouteResp resp) {
                        resp.parsePoints();
                        getView().loadPtAroundPoints(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().loadPtFailed(ErrorHandler.handle(e, null), "ptarrount");
                    }
                }));
    }

    public void loadDestinationSceneList(int sceneid, int withscene) {
        boolean isDebug = cacheMgr().getSPBoolean(AppConstants.DEBUG, false);
        api().loadDestinationSceneList(wrapper().getUserId(), sceneid, withscene, isDebug ? 1 : 0)
                .map(new HttpService.HttpResultMap<GpsRouteResp>())
                .compose(new HttpService.CommonScheduler<GpsRouteResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<GpsRouteResp>() {
                    @Override
                    public void onSuccess(GpsRouteResp resp) {
                        resp.parseRadio();
                        getView().loadDestinationList(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().loadPtFailed(ErrorHandler.handle(e, null), "destlist");
                    }
                }));
    }

    public void loadAudioByScene(int sceneid) {
        api().loadAudioByScene(wrapper().getUserId(), sceneid)
                .map(new HttpService.HttpResultMap<GpsRouteResp>())
                .compose(new HttpService.CommonScheduler<GpsRouteResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<GpsRouteResp>() {
                    @Override
                    public void onSuccess(GpsRouteResp resp) {
                        getView().loadSceneAudios(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().loadPtFailed(ErrorHandler.handle(e, null), "sceneaudio");
                    }
                }));
    }

    public void loadSimuNaviConfig() {
        //String simuNaviCfgKey = "key_simuNaviCfgKey";
        api().loadSimucfg(wrapper().getUserId())
                .map(new HttpService.HttpResultMap<SimuNavicfgResp>())
                .compose(new HttpService.CommonScheduler<SimuNavicfgResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<SimuNavicfgResp>() {
                    @Override
                    public void onSuccess(SimuNavicfgResp resp) {
                        getView().loadSimuConfig(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().loadPtFailed(ErrorHandler.handle(e, null), "simucfg");
                    }
                }));
    }
}

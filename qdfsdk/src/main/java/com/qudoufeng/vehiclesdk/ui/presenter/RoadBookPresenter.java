package com.qudoufeng.vehiclesdk.ui.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.qudoufeng.vehiclesdk.base.IBasePresenter;
import com.qudoufeng.vehiclesdk.http.ErrorHandler;
import com.qudoufeng.vehiclesdk.http.HttpService;
import com.qudoufeng.vehiclesdk.http.subscribers.EmptySubscriber;
import com.qudoufeng.vehiclesdk.http.subscribers.SubscriberListener;
import com.qudoufeng.vehiclesdk.model.GetGpsRouteModel;
import com.qudoufeng.vehiclesdk.model.GpsRouteResp;
import com.qudoufeng.vehiclesdk.model.RoadBookResp;
import com.qudoufeng.vehiclesdk.ui.view.RoadBookViewI;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by WanYang on 2018/7/10/010.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class RoadBookPresenter extends IBasePresenter<RoadBookViewI> {

    public RoadBookPresenter(RoadBookViewI view) {
        attech(view);
    }

    public void loadWayList(final int page, final String ftype, final int tagid, String location) {
        boolean debug = cacheMgr().getSPBoolean(AppConstants.DEBUG, false);
        api().getRoadBookList(wrapper().getUserId(), page, debug ? 10 : 0, ftype, tagid, location)
                .map(new HttpService.HttpResultMap<RoadBookResp>())
                .compose(new HttpService.CommonScheduler<RoadBookResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<RoadBookResp>() {
                    @Override
                    public void onSuccess(RoadBookResp resp) {
                        if (resp.waylist != null && resp.waylist.size() > 0 && page == 0) {
                            cacheMgr().setMMString(Utils.getHomeTripListCacheKey(ftype, tagid), new Gson().toJson(resp));
                        }
                        getView().loadWayList(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        String json = cacheMgr().getMMString(Utils.getHomeTripListCacheKey(ftype, tagid), null);
                        if (!TextUtils.isEmpty(json)) {
                            try {
                                RoadBookResp res = new Gson().fromJson(json, RoadBookResp.class);
                                if (res != null && page == 0) {
                                    getView().loadWayList(res);
                                }
                            } catch (Exception ep) {
                            }
                        } else {
                            getView().requestFailed(ErrorHandler.handle(e, null));
                        }
                    }
                }));
    }

    public void loadDetail(final int wid) {
        api().getRoadDetail(wrapper().getUserId(), wid)
                .map(new HttpService.HttpResultMap<RoadBookResp>())
                .compose(new HttpService.CommonScheduler<RoadBookResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<RoadBookResp>() {
                    @Override
                    public void onSuccess(RoadBookResp resp) {
                        cacheMgr().setMMString(Utils.getTripDetailCacheKey(wid, wrapper().getUserId()), new Gson().toJson(resp));
                        resp.parseWay();
                        getView().loadDetail(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        String json = cacheMgr().getMMString(Utils.getTripDetailCacheKey(wid, wrapper().getUserId()), null);
                        if (!TextUtils.isEmpty(json)) {
                            try {
                                RoadBookResp res = new Gson().fromJson(json, RoadBookResp.class);
                                if (res != null) {
                                    res.parseWay();
                                    getView().loadDetail(res);
                                }
                            } catch (Exception ep) {}
                        } else {
                            getView().requestFailed(ErrorHandler.handle(e, null));
                        }
                    }
                }));
    }

    public void loadViewPoints(final int wid, final int only, int withloc) {
        RequestBody body = null;
        try {
            JSONObject obj = new JSONObject();
            obj.put("sceneonly", only);
            obj.put("withloc", withloc);
            obj.put("withsubway", 1);

            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (null == body) {
            return;
        }

        api().getTripPoints(wrapper().getUserId(), wid, body)
                .map(new HttpService.HttpResultMap<>())
                .compose(new HttpService.CommonScheduler<>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<GpsRouteResp>() {
                    @Override
                    public void onSuccess(GpsRouteResp resp) {
                        cacheMgr().setMMString(Utils.getBroadCastCacheKey(wrapper().getUserId(), wid), new Gson().toJson(resp));
                        resp.parsePoints();
                        getView().loadViewPoints(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        String json = cacheMgr().getMMString(Utils.getBroadCastCacheKey(wrapper().getUserId(), wid), null);
                        if (!TextUtils.isEmpty(json)) {
                            GpsRouteResp resp = null;
                            try {
                                resp = new Gson().fromJson(json, GpsRouteResp.class);
                            } catch (Exception ep) {
                            }
                            if (resp != null) {
                                resp.parsePoints();
                                if (resp.isActived()) {
                                    //缓存未过期
                                    int lock = 0;
                                    if (resp.pointlist != null) {
                                        for (GetGpsRouteModel item : resp.pointlist) {
                                            if (lock == 0) {
                                                if (item.isBigPoint()) {
                                                    lock = 1;
                                                }
                                                continue;
                                            }
                                            if (!TextUtils.isEmpty(item.resid)) {
                                                item.resid = "";
                                                item.lock = 1;
                                            }
                                            if (!TextUtils.isEmpty(item.preresid)) {
                                                item.preresid = "";
                                                item.lockpre = 1;
                                            }
                                        }
                                    }
                                } else {
                                    resp.pointlist = new ArrayList<>();
                                }
                                getView().loadViewPoints(resp);
                                return;
                            }
                        }
                        getView().requestFailed(ErrorHandler.handle(e, null));
                    }
                }));
    }
}

package com.qudoufeng.vehiclesdk.ui.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.qudoufeng.vehiclesdk.base.IBasePresenter;
import com.qudoufeng.vehiclesdk.http.ErrorHandler;
import com.qudoufeng.vehiclesdk.http.HttpService;
import com.qudoufeng.vehiclesdk.http.subscribers.EmptySubscriber;
import com.qudoufeng.vehiclesdk.http.subscribers.SubscriberListener;
import com.qudoufeng.vehiclesdk.model.VipResp;
import com.qudoufeng.vehiclesdk.ui.view.VipInfoViewI;
import com.qudoufeng.vehiclesdk.utils.Utils;

/**
 * Created by WanYang on 2019/1/18/018.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class VipInfoPresenter extends IBasePresenter<VipInfoViewI> {

    public VipInfoPresenter(VipInfoViewI view) {
        attech(view);
    }

    public void loadUserVipInfo() {
        api().loadUserVipInfo(wrapper().getUserId())
                .map(new HttpService.HttpResultMap<VipResp>())
                .compose(new HttpService.CommonScheduler<VipResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<VipResp>() {
                    @Override
                    public void onSuccess(VipResp resp) {
                        cacheMgr().setMMString(Utils.getVipInfoCacheKey(wrapper().getUserId()), new Gson().toJson(resp));
                        getView().loadUserVipInfo(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        String json = cacheMgr().getMMString(Utils.getVipInfoCacheKey(wrapper().getUserId()), "");
                        if (!TextUtils.isEmpty(json)){
                            try {
                                VipResp resp = new Gson().fromJson(json, VipResp.class);
                                if (resp != null) {
                                    getView().loadUserVipInfo(resp);
                                    return;
                                }
                            } catch (Exception ep) {}
                        }
                        getView().onVipError(ErrorHandler.handle(e, null));
                    }
                }));
    }
}

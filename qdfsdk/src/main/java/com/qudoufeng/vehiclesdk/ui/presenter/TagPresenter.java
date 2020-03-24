package com.qudoufeng.vehiclesdk.ui.presenter;

import com.qudoufeng.vehiclesdk.base.IBasePresenter;
import com.qudoufeng.vehiclesdk.http.ErrorHandler;
import com.qudoufeng.vehiclesdk.http.HttpService;
import com.qudoufeng.vehiclesdk.http.subscribers.EmptySubscriber;
import com.qudoufeng.vehiclesdk.http.subscribers.SubscriberListener;
import com.qudoufeng.vehiclesdk.model.tag.AppTagResp;
import com.qudoufeng.vehiclesdk.ui.view.TagViewI;
import com.qudoufeng.vehiclesdk.utils.Utils;

/**
 * Created by WanYang on 2019/1/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class TagPresenter extends IBasePresenter<TagViewI> {

    public TagPresenter(TagViewI view) {
        attech(view);
    }

    public void loadHomeTags() {
        api().loadHomeTags(wrapper().getUserId())
                .map(new HttpService.HttpResultMap<AppTagResp>())
                .compose(new HttpService.CommonScheduler<AppTagResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<AppTagResp>() {
                    @Override
                    public void onSuccess(AppTagResp o) {
                        cacheMgr().setMMObject(Utils.getHomeTagCacheKey(), o);
                        getView().loadHomeTags(o);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        AppTagResp resp = (AppTagResp)cacheMgr().getMMObject(Utils.getHomeTagCacheKey(), AppTagResp.class);
                        if (resp != null) {
                            getView().loadHomeTags(resp);
                        } else {
                            getView().requestFailed(ErrorHandler.handle(e, null));
                        }
                    }
                }));
    }

    public void loadAllTags(String filter) {
        api().loadAllTags(wrapper().getUserId(), filter)
                .map(new HttpService.HttpResultMap<AppTagResp>())
                .compose(new HttpService.CommonScheduler<AppTagResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<AppTagResp>() {
                    @Override
                    public void onSuccess(AppTagResp resp) {
                        cacheMgr().setMMObject(Utils.getAllTagCacheKey(), resp);
                        getView().loadAllTags(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        AppTagResp resp = (AppTagResp)cacheMgr().getMMObject(Utils.getAllTagCacheKey(), AppTagResp.class);
                        if (resp != null) {
                            getView().loadAllTags(resp);
                        } else {
                            getView().requestFailed(ErrorHandler.handle(e, null));
                        }
                    }
                }));
    }
}

package com.qudoufeng.vehiclesdk.ui.presenter;


import com.qiniu.android.utils.StringUtils;
import com.qudoufeng.vehiclesdk.base.IBasePresenter;
import com.qudoufeng.vehiclesdk.http.ErrorHandler;
import com.qudoufeng.vehiclesdk.http.HttpService;
import com.qudoufeng.vehiclesdk.http.subscribers.EmptySubscriber;
import com.qudoufeng.vehiclesdk.http.subscribers.SubscriberListener;
import com.qudoufeng.vehiclesdk.model.LoginReq;
import com.qudoufeng.vehiclesdk.model.LoginResp;
import com.qudoufeng.vehiclesdk.ui.view.LoginViewI;

/**
 * Created by WanYang on 2018/7/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class LoginPresenter extends IBasePresenter<LoginViewI> {

    public LoginPresenter(LoginViewI view) {
        attech(view);
    }

    public void login(LoginReq body) {
        api().postLogin(body)
                .map(new HttpService.HttpResultMap<LoginResp>())
                .compose(new HttpService.CommonScheduler<LoginResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<LoginResp>() {
                    @Override
                    public void onSuccess(LoginResp resp) {
                        getView().login(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().requestFailed(ErrorHandler.handle(e, null));
                    }
                }));
    }

    public void quickLogin(String vToken, String vCode, String userid, LoginReq body) {
        api().quickLogin(vToken, vCode, userid, body)
                .map(new HttpService.HttpResultMap<LoginResp>())
                .compose(new HttpService.CommonScheduler<LoginResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<LoginResp>() {
                    @Override
                    public void onSuccess(LoginResp resp) {
                        getView().login(resp);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().requestFailed(ErrorHandler.handle(e, null));
                    }
                }));
    }

    // 二维码登录
    public void qrLogin(String acode) {
        if (StringUtils.isNullOrEmpty(acode)) {
            acode = "newlogin";
        }
        api().qrlogin(acode)
                .map(new HttpService.HttpResultMap<LoginResp>())
                .compose(new HttpService.CommonScheduler<LoginResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<LoginResp>() {
                    @Override
                    public void onSuccess(LoginResp resp) {
                        if (!StringUtils.isNullOrEmpty(resp.acode)) {
                            getView().qrcode(resp.acode);
                        } else {
                            getView().login(resp);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().requestFailed(ErrorHandler.handle(e, null));
                    }
                }));
    }

}

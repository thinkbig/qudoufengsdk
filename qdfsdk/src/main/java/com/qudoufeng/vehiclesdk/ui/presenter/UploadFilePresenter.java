package com.qudoufeng.vehiclesdk.ui.presenter;


import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qudoufeng.vehiclesdk.DFSdk;
import com.qudoufeng.vehiclesdk.base.IBasePresenter;
import com.qudoufeng.vehiclesdk.http.ErrorHandler;
import com.qudoufeng.vehiclesdk.http.HttpService;
import com.qudoufeng.vehiclesdk.http.subscribers.EmptySubscriber;
import com.qudoufeng.vehiclesdk.http.subscribers.SubscriberListener;
import com.qudoufeng.vehiclesdk.model.GetFileTokenReq;
import com.qudoufeng.vehiclesdk.model.GetFileTokenResp;
import com.qudoufeng.vehiclesdk.ui.view.UploadViewI;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.BuildConfig;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WanYang on 2018/7/27/027.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class UploadFilePresenter extends IBasePresenter<UploadViewI> {
    private UploadManager uploadManager;

    public UploadFilePresenter(UploadViewI view) {
        attech(view);
    }

    // 上传log文件, logtype(0=普通log，1=gpslog)
    public boolean uploadLogFile(final File logfile, final int logtype, final long starttime, final long endtime) {
        if (null == logfile || !logfile.exists() || logfile.isDirectory()) {
            return false;
        }

        GetFileTokenReq body = new GetFileTokenReq();
        body.count = 1;
        body.format = AppConstants.ResTypeLOG;
        body.imagetype = logtype == 1 ? "gps" : "log";
        api().getFileToken(body)
                .map(new HttpService.HttpResultMap<GetFileTokenResp>())
                .compose(new HttpService.CommonScheduler<GetFileTokenResp>())
                .subscribe(new EmptySubscriber<>(new SubscriberListener<GetFileTokenResp>() {
                    @Override
                    public void onSuccess(GetFileTokenResp resp) {
                        Map<String, String> params = new HashMap<>();
                        params.put("x:type", logtype + "");
                        params.put("x:source", BuildConfig.APK_SOURCE + "");
                        if (wrapper().getUserId() == null || wrapper().getUserId().length() == 0) {
                            params.put("x:userid", null);
                        } else {
                            params.put("x:userid", wrapper().getUserId());
                        }
                        params.put("x:deviceid", wrapper().getUdid());

                        params.put("x:starttime", starttime + "");
                        params.put("x:endtime", endtime + "");

                        UploadOptions options = new UploadOptions(params, null, true, null, null);
                        uploadToQiNiu(logfile, resp.upload.resids.get(0), resp.upload.token, options, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (getView() == null) return;
                                if (info.isOK()) {
                                    logfile.delete();
                                    getView().onUploadSuccess(key);
                                } else {
                                    getView().onUploadError(info.error == null ? "七牛上传失败" : "七牛_" + info.error);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        if (getView() == null) return;
                        getView().onUploadError(ErrorHandler.handle(e, null));
                    }
                }));
        return true;
    }

    private void uploadToQiNiu(File file, String resid, String token, UploadOptions options, final UpCompletionHandler callBack) {
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        if (uploadManager == null)
            uploadManager = new UploadManager(DFSdk.getQNconfig());
        uploadManager.put(file, resid, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                callBack.complete(key, info, response);
            }
        }, options);
    }
}

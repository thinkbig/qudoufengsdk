package com.qudoufeng.vehiclesdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.qudoufeng.vehiclesdk.eventmsg.EventMessage;
import com.qudoufeng.vehiclesdk.manager.PostLogManager;
import com.qudoufeng.vehiclesdk.model.LoginReq;
import com.qudoufeng.vehiclesdk.sdkpublic.DFFactory;
import com.qudoufeng.vehiclesdk.utils.ActivityManager;
import com.qudoufeng.vehiclesdk.utils.filelogger.FL;

import org.greenrobot.eventbus.EventBus;

import static com.qudoufeng.vehiclesdk.eventmsg.EventMessage.Type.TOBACKGROUND;

// 打包lib， 发布到 jcenter： https://www.jianshu.com/p/4a37c0db3edd
// 全部打包到本地  https://www.jianshu.com/p/71058db2e429

/**
 * Created by WanYang on 2018/7/4/004.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class MyApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private int activityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        // 需要升级rxjava，处理crash  https://www.jianshu.com/p/436cb79eace5

        DFFactory.getSdk(getApplicationContext()).initSdk("any");

        registerActivityLifecycleCallbacks(this);

        UserWrapper wrapper = DFSdk.getUwrapper().wrapper;
        wrapper.fetchAppConfig();
        if (!wrapper.isLogin()) {
            forceLogin();
        } else {
            wrapper.asyncAccountInfo();
        }

        FL.d("============ App onCreate =============");
    }

    private void forceLogin() {
        UserWrapper wrapper = DFSdk.getUwrapper().wrapper;
        LoginReq loginBody = new LoginReq();
        loginBody.phone = "30000000001";
        loginBody.password = "Asdf1234";
        loginBody.type = "phone";
        wrapper.relogin(loginBody);
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        DFSdk.getInstance(this).applicationRunForeground();
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        DFSdk.getInstance(this).applicationRunBackground();
        FL.d("============ leaveApp =============");

        PostLogManager.getInstance(this).checkGpsLogFile();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        activityCount++;
        if (DFSdk.appRunningStat() != 1) {
            //应用从后台回到前台 需要做的操作
            back2App(activity);
            FL.d("============ back2App =============");
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ActivityManager.getInstance().setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityCount--;
        if (activityCount == 0) {
            //应用进入后台 需要做的操作
            leaveApp(activity);

            EventMessage message = new EventMessage();
            message.type = TOBACKGROUND;
            EventBus.getDefault().post(message);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityManager.getInstance().removeActivity(activity);
    }

    /**
     * 如果使用了MultiDex，建议通过Gradle的“multiDexKeepFile”配置等方式把Bugly的类放到主Dex，
     * 另外建议在Application类的"attachBaseContext"方法中主动加载非主dex：
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

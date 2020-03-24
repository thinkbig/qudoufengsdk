package com.qudoufeng.vehiclesdk;

/*
sdk功能定义：
通用服务：
1、登录注册
2、账号同步（账户信息，登录态，会员状态）

导游服务
1、启动导游播报服务
2、启动导游浮窗
3、暂停导游播报服务
4、继续导游播报服务
5、结束导游播报服务

其他服务
1、获取线路分类
2、获取线路列表
3、获取线路详情

TODO
1、vip购买续费

 */

import android.content.Context;
import android.content.Intent;

import com.qiniu.android.storage.Configuration;
import com.qudoufeng.vehiclesdk.dagger.AppModule;
import com.qudoufeng.vehiclesdk.dagger.DaggerRoadBookComponent;
import com.qudoufeng.vehiclesdk.dagger.RoadBookComponent;
import com.qudoufeng.vehiclesdk.manager.CacheManager;
import com.qudoufeng.vehiclesdk.manager.PostLogManager;
import com.qudoufeng.vehiclesdk.model.Version;
import com.qudoufeng.vehiclesdk.model.triplog.TripStatLogger;
import com.qudoufeng.vehiclesdk.sdkpublic.IDFSdk;
import com.qudoufeng.vehiclesdk.service.DFTourService;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.StorageConstants;
import com.qudoufeng.vehiclesdk.utils.filelogger.FL;
import com.qudoufeng.vehiclesdk.utils.filelogger.FLConfig;
import com.qudoufeng.vehiclesdk.utils.filelogger.FLConst;
import com.qudoufeng.vehiclesdk.utils.filelogger.FLGps;

import java.io.File;

// 要改成工厂模式，隐藏具体实现
public class DFSdk implements IDFSdk {

    private static DFSdk instance;
    private Context context;
    private RoadBookComponent mComponent;
    private Configuration QNconfig;
    private UtilityWrapper uwrapper;

    private TripStatLogger tripStatLogger;
    private int appRunStat = 0;    //0 未知，1 前台，2 后台
    private boolean isInited = false;

    public static DFSdk getInstance(Context context) {
        if (instance == null) {
            instance = new DFSdk(context);
        }
        return instance;
    }

    private DFSdk(Context context) {
        this.context = context;
        this.mComponent = DaggerRoadBookComponent.builder()
                .appModule(new AppModule(context))
                .build();
    }

    private void initSdk() {
        if (isInited) return;

        isInited = true;

        this.uwrapper = new UtilityWrapper();
        this.uwrapper.wrapper.fetchAppConfig();

        // 初始化各个文件夹
        StorageConstants.initStoragePath(context, true);
        initFileLogger();
        initQiNiuSDK();

        tripStatLogger = new TripStatLogger(context);

        FL.d("============ SDK init =============");
    }

    private void initFileLogger() {
        // 几个关键参数，将来要从服务器读取
        boolean enablefilelog = false;
        boolean enableGPSfilelog = false;
        boolean enableConsolelog = false;
        int logLevel = FLConst.Level.V;
        int gpslevel = FLConst.Level.V;
        if (null != uwrapper.wrapper.getAppConfig()) {
            Version version = uwrapper.wrapper.getAppConfig().version;
            if (null != version) {
                if (version.loglevel >= 0) {
                    enablefilelog = true;
                    logLevel = version.loglevel;
                }
                if (version.gpslevel >= 0) {
                    enableGPSfilelog = true;
                    gpslevel = version.loglevel;
                }
                version.applyConfig();
            }
        }

        if (isDebug()) {
            enablefilelog = true;
            enableGPSfilelog = true;
        }

        // 每小时一个log文件，自动分卷
        // 文件log配置，默认关闭文件log开关，就是普通控制台log
        // TODO 后台服务器推送文件log开关，打开该开关的用户，会开启文件log并上报
        FL.init(new FLConfig.Builder(context)
                .defaultTag("DF")   // customise default tag
                .logRawStr(false)
                .minLevel(logLevel)   // customise minimum logging level
                .logToFile(false)   // enable logging to file
                .dir(new File(StorageConstants.SDCARD_LOGS))    // customise directory to hold log files
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT) // customise retention strategy
                .maxFileCount(FLConst.DEFAULT_MAX_FILE_COUNT)    // 7天,  因为用户晚上不可能用，实际是14天
                .maxTotalSize(FLConst.DEFAULT_MAX_TOTAL_SIZE)    // 128m
                .build());
        FL.setEnabled(true);    // 默认打开，但是否写入文件由服务器控制
        FL.setEnableConsoleLog(enableConsolelog);
        FL.setEnableFileLog(enablefilelog);

        // GPS文件log
        // TODO 后台服务器推送文件log开关，打开该开关的用户，会开启文件log并上报
        FLGps.init(new FLConfig.Builder(context)
                .consoleLogger(null)    // 关闭控制台log
                .defaultTag("GPS")   // customise default tag
                .logRawStr(true)
                .minLevel(gpslevel)   // customise minimum logging level
                .logToFile(true)   // enable logging to file
                .dir(new File(StorageConstants.TRIPLOCATIONCACHE))    // customise directory to hold log files
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT) // customise retention strategy
                .maxFileCount(24 * 20)    // 15天， 因为用户晚上不可能用，实际是30天
                .maxTotalSize(FLConst.DEFAULT_MAX_TOTAL_SIZE)    // 128m
                .build());
        // GPSlog 默认关闭，只写文件，因此只需要控制总开关
        FLGps.setEnabled(enableGPSfilelog);

        // 测试文件log
//        for (int i=0; i<100; i++) {
//            FLGps.e("gps,gps,asdfsdf");
//            FL.e("testeeee," + i);
//        }
//        FLGps.resetFiletag("e2");       // 强制使用新文件
//        FL.setEnableFileLog(true);  // 启用文件log
//        for (int i=0; i<100; i++) {
//            FL.e("123123123");
//            FLGps.e("123123");
//        }
    }

    private void initQiNiuSDK() {
        QNconfig = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
                .build();
    }


    /////////////////////////////////////////////////////////////////////////////

    static public boolean isDebug() {
        if (null != instance && null != instance.uwrapper) {
            CacheManager manager = instance.uwrapper.cacheManager;
            return manager.getSPBoolean(AppConstants.DEBUG, false);
        }
        return false;
    }

    static public RoadBookComponent component() {
        return null != instance ? instance.mComponent : null;
    }

    static public Context getContext() {
        return null != instance ? instance.context : null;
    }

    static public TripStatLogger getTripStatLogger() {
        return null != instance ? instance.tripStatLogger : null;
    }

    static public int appRunningStat() {
        return null != instance ? instance.appRunStat : null;
    }

    static public UtilityWrapper getUwrapper() {
        return null != instance ? instance.uwrapper : null;
    }


    static public Configuration getQNconfig() {
        return null != instance ? instance.QNconfig : null;
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initSdk(String authkey) {
        initSdk();
    }

    @Override
    public void startDFService(boolean autoStart, boolean showFloatWindow) {
        if (DFTourService.isStarted) {
            return;
        }
        Intent intent = new Intent(context, DFTourService.class);
        intent.putExtra("isHideView", !showFloatWindow);
        intent.putExtra("autoStart", autoStart);

        // 分别是服务启动，和绑定启动
        context.startService(intent);
    }

    @Override
    public void stopDFService() {
        Intent intent = new Intent(context, DFTourService.class);
        context.stopService(intent);
    }

    @Override
    public void applicationRunForeground() {
        appRunStat = 1;
    }

    @Override
    public void applicationRunBackground() {
        appRunStat = 2;
        PostLogManager.getInstance(context).checkGpsLogFile();
    }
}

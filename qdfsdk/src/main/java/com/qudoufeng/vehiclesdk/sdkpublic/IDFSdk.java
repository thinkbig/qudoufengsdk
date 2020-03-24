package com.qudoufeng.vehiclesdk.sdkpublic;

/**
 * 趣兜风CoreSdk
 */
public interface IDFSdk {

    /**
     * sdk初始化
     *
     * @param authkey the authkey
     */
    void initSdk(String authkey);

    /**
     * 直接启动导游播报服务
     * 也可以直接绑定 DFTourService 实例来实现更丰富的功能
     *
     * @param autoStart       是否自动启动导游服务
     * @param showFloatWindow 是否显示悬浮窗，需要悬浮窗权限
     */
    void startDFService(boolean autoStart, boolean showFloatWindow);

    /**
     * 停止自动导游服务.
     */
    void stopDFService();

    /**
     * Application 回到前台/或首次打开时调用
     */
    void applicationRunForeground();

    /**
     * Application 到后台时调用
     */
    void applicationRunBackground();
}

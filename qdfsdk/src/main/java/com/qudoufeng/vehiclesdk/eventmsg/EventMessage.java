package com.qudoufeng.vehiclesdk.eventmsg;

/**
 * Created by WanYang on 2018/7/11/011.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class EventMessage {
    public Type type;
    public String msg;

    public EventMessage() {
    }

    public EventMessage(Type type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public enum Type{
        LOGIN,
        LOGIN_EXPIRE,   // 登陆过期，要重新登陆，code=8
        TAB_2,
        REFRESH_PROFILE,
        CHANGE_TAB,
        POINTDETAIL,
        REFRESHROADBOOK,
        PROFILE,
        DEBUG,
        STARTLOCATION,
        STOPLOCATION,
        REFRESHLOCATION,
        RESETBTN,
        OPENLOCATION,
        RETRYTRIP,
        FinishDetail,
        FinishPackAge,
        PaySuccess,
        Background,
        BeginNavi,
        EndNavi,
        TOBACKGROUND,
        TOFRONT,
        DownSuccess,
        RefreshComment,
        PlayAudio,
        OnPtArroundUpdate,      // 导游播报，附近点列表更新（用于地图展示）
        ChangeAudio, // 切换音频，（用于获取已播放音频列表）
        SimuNavigation, // 开启模拟导航
        OnNeedVip,   // 遇到需要vip才能访问的资源
    }
}

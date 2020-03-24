package com.qudoufeng.vehiclesdk.model.triplog;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.qudoufeng.vehiclesdk.DFSdk;
import com.qudoufeng.vehiclesdk.UserWrapper;
import com.qudoufeng.vehiclesdk.manager.CacheManager;
import com.qudoufeng.vehiclesdk.manager.PostLogManager;
import com.qudoufeng.vehiclesdk.model.GetGpsRouteModel;
import com.qudoufeng.vehiclesdk.model.RoadBookModel;
import com.qudoufeng.vehiclesdk.model.random.TripCustomEvent;
import com.qudoufeng.vehiclesdk.utils.FileManager;
import com.qudoufeng.vehiclesdk.utils.StorageConstants;
import com.qudoufeng.vehiclesdk.utils.TimeUtil;
import com.qudoufeng.vehiclesdk.utils.filelogger.FL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by WanYang on 2018/11/5/005.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */
public class TripStatLogger {
    @Inject
    CacheManager cacheManager;

    @Inject
    UserWrapper wrapper;

    private String UnfinishedKey = "kUnfinishedTrip";

    private Boolean isDebug;
    public String _rootDir;
    public TripLogModel curLogModel;
    // 实际经过的大小景点、语音点
    public List<TripLogItem> meetItems;
    // 实际播放的语音(不包含gps信息)
    public List<TripLogItem> playItems;

    // 实时gps1分钟上报一次，记录上一次上报时间
    private long reportBegin;

    private long lastLogTime;

    private boolean isAutoTest;
    private Context mContent;


    public void setAutoTest(boolean autoTest) {
        isAutoTest = autoTest;
    }

    public TripStatLogger(Context content) {
        mContent = content;
        DFSdk.component().inject(this);
    }

    public void startRandomTrip(int wid) {
        String userid = wrapper.getUserId();
        if (curLogModel != null) {
            saveModel(true);
        }
        curLogModel = new TripLogModel(userid, "智能导游", wid);
        meetItems = new ArrayList<>();
        playItems = new ArrayList<>();
        curLogModel.playitems = playItems;
    }

    public void logStartTrip(RoadBookModel item, int itemId) {
        String userid = wrapper.getUserId();
        if (item == null) return;
        if (curLogModel != null) {
            if (curLogModel.wid == item.wid || !curLogModel.userid.equals(userid)) {
                saveModel(true);
                initTripLogger(item, userid, itemId);
            }
        } else {
            initTripLogger(item, userid, itemId);
        }
        //firstitem 表示用户最近一次选择的出发点，用于继续行程
        curLogModel.firstitem = itemId;

        TripCustomEvent event = new TripCustomEvent(2, wrapper.getUserId());
        event.type = 1;
        event.wayid = item.id;
        PostLogManager.getInstance(mContent).reportEvent(event);
    }

    public void setAllAudioList(RoadBookModel trip, List<GetGpsRouteModel> allAudioPoints) {
        if (playItems != null && playItems.size() > 0) {
            return;
        }

        List<TripLogItem> temArr = new ArrayList<>();
        if (!TextUtils.isEmpty(trip.audiobegin)) {
            TripLogItem logItem = new TripLogItem(TripLogItem.eAudioLogTypeBegin, trip.audiobegin);
            logItem.title = "片头";
            temArr.add(logItem);
        }
        if (!TextUtils.isEmpty(trip.introsound)) {
            TripLogItem logItem = new TripLogItem(TripLogItem.eAudioLogTypeIntro, trip.introsound);
            logItem.title = "线路介绍";
            temArr.add(logItem);
        }

        Collections.sort(allAudioPoints, new Comparator<GetGpsRouteModel>() {
            @Override
            public int compare(GetGpsRouteModel left, GetGpsRouteModel right) {
                if ((left.rate - right.rate) > 0) {
                    return 1;
                } else if ((left.rate - right.rate) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        for (GetGpsRouteModel item : allAudioPoints) {
            int logType = TripLogItem.eAudioLogTypeScene;
            if (item.isScenept()) {
                logType = TripLogItem.eAudioLogTypeScene;
            } else if (item.isAudiopt()) {
                logType = TripLogItem.eAudioLogTypeAudio;
            } else {
                continue;
            }

            if (!TextUtils.isEmpty(item.resid)) {
                TripLogItem logItem = new TripLogItem(logType, item.resid);
                logItem.title = TextUtils.isEmpty(item.name) ? item.title : item.name;
                logItem.id = item.id;
                temArr.add(logItem);
            }
        }

        if (!TextUtils.isEmpty(trip.audioend)) {
            TripLogItem logItem = new TripLogItem(TripLogItem.eAudioLogTypeEnd, trip.audioend);
            logItem.title = "片尾";
            temArr.add(logItem);
        }
        playItems = temArr;
        FL.d("playItems==" + playItems.size());
    }


    public boolean isDebug() {
        if (null == isDebug) {
            isDebug = DFSdk.isDebug();
        }
        return isDebug;
    }

    private String getAudioResId(String resid) {
        if (TextUtils.isEmpty(resid)) {
            return "";
        }
        return resid.split("\\.")[0];
    }

    /**
     * @param resid
     * @param logType
     * @param audioStatus
     * @param mpid        传进来点的id
     */
    public void logAudio(String resid, int logType, int audioStatus, int mpid) {
        if (playItems != null && playItems.size() > 0) {
            TripLogItem exist = null;
            for (TripLogItem item : playItems) {
                if (mpid > 0) {
                    if (item.id == mpid && getAudioResId(resid).equals(getAudioResId(item.resid))) {
                        exist = item;
                        break;
                    }
                }
                if (getAudioResId(resid).equals(getAudioResId(item.resid))) {
                    exist = item;
                    break;
                }
            }

            if (exist != null) {
                exist.status = audioStatus;
            } else {
                exist = new TripLogItem(logType, resid);
                exist.title = "未知";
                exist.status = audioStatus;
                exist.id = mpid;
                playItems.add(exist);
            }

            saveModel(false);
        }
        //记录当天介绍语音播放情况
        if (curLogModel != null && TripLogItem.eAudioLogStatFinish == audioStatus &&
                (logType == TripLogItem.eAudioLogTypeBegin || logType == TripLogItem.eAudioLogTypeIntro)) {
            savePlayedTime(resid, curLogModel.wid);
        }

    }

    /**
     * 记录并上报事件
     *
     * @param logType
     * @param audioStatus
     */
    public void logAndReportAudio(GetGpsRouteModel item, int logType, int audioStatus) {
        int mpid = item.id;
        String resid = item.resid;
        TripLogItem exist = null;
        if (null == playItems) {
            playItems = new ArrayList<>();
        }

        if (playItems.size() > 0) {
            for (TripLogItem object : playItems) {
                if (mpid > 0) {
                    if (object.id == mpid && getAudioResId(resid).equals(getAudioResId(object.resid))) {
                        exist = object;
                        break;
                    }
                } else if (getAudioResId(resid).equals(getAudioResId(object.resid))) {
                    exist = object;
                    break;
                }
            }
        }

        if (exist != null) {
            exist.status = audioStatus;
        } else {
            exist = new TripLogItem(logType, resid);
            exist.title = null != item.title ? item.title : "未知";
            exist.status = audioStatus;
            exist.id = mpid;
            playItems.add(exist);
        }

        saveModel(false);

        //记录当天介绍语音播放情况
        if (curLogModel != null && TripLogItem.eAudioLogStatFinish == audioStatus &&
                (logType == TripLogItem.eAudioLogTypeBegin || logType == TripLogItem.eAudioLogTypeIntro)) {
            savePlayedTime(resid, curLogModel.wid);
        }

        if (null == curLogModel || curLogModel.isEmulator) return;

        // 事件实时上报
        // 0=gps(这时mpid只表示最近点),1=给gps权限，2=导游开始，3=导游结束, 4=轨迹纪录开始，5=轨迹纪录停止, 6=gps轨迹事件, 7=导游暂停 10=语音加入队列，11=语音开始播放，12=播放结束，13=播放失败，20=继续播放，21=用户暂停，22=短暂停（电话，外部导航等），23=qq音乐等事件
        TripCustomEvent event = new TripCustomEvent(0, wrapper.getUserId());
        event.type = 1;
        event.wayid = curLogModel.wid;

        if (TripLogItem.eAudioLogStatQuene == audioStatus) {
            event.event = 10;
        } else if (TripLogItem.eAudioLogStatBegin == audioStatus) {
            event.event = 11;
        } else if (TripLogItem.eAudioLogStatFinish == audioStatus) {
            event.event = 12;
        } else if (TripLogItem.eAudioLogStatFail == audioStatus) {
            event.event = 13;
        }
        event.updateWithItem(item);

        PostLogManager.getInstance(mContent).reportEvent(event);
    }


    public void logArrivePoint(GetGpsRouteModel item) {
        if (null != item && null != meetItems) {
            for (TripLogItem log : meetItems) {
                if (log.id == item.id) {
                    return;
                }
            }
            TripLogItem logitem = new TripLogItem(item);
            meetItems.add(logitem);
            saveModel(false);
        }
    }

    public void setGpsLogFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            if (curLogModel != null) {
                if (curLogModel.gpslogfile == null) {
                    curLogModel.gpslogfile = new ArrayList<>();
                }
                curLogModel.gpslogfile.add(filePath);
                curLogModel.gpslogfile = new ArrayList<>(new HashSet<>(curLogModel.gpslogfile));
            }
        }
    }

    public void setIsEmulator(boolean isEmulator) {
        if (curLogModel != null) {
            curLogModel.isEmulator = isEmulator;
        }
    }

    public TripLogItem lastPlayeItem(String resid) {
        if (curLogModel != null) {
            return curLogModel.lastPlayeItem(resid);
        }
        return null;
    }

    public TripLogModel penddingTrip() {
        if (null == curLogModel) {
            loadUnfinishedModel();
        }
        if (curLogModel != null && isTripBegin()) {
            return curLogModel;
        }
        return null;
    }

    public TripLogModel ongoingTrip() {
        if (curLogModel != null && isTripBegin()) {
            return curLogModel;
        }
        return null;
    }

    public TripLogItem lastScene() {
        TripLogItem last = null;
        for (int index = meetItems.size() - 1; index >= 0; index--) {
            TripLogItem obj = meetItems.get(index);
            if (obj.isScenept()) {
                last = obj;
                break;
            }
        }
        return last;
    }

    public boolean isAudioPlayed(String resid) {
        TripLogItem exist = lastPlayeItem(resid);
        return null != exist && (exist.status == TripLogItem.eAudioLogStatFinish || exist.status == TripLogItem.eAudioLogStatReplace);
    }

    public TripLogItem isArrived(int mpid) {
        TripLogItem item = null;
        for (TripLogItem model : meetItems) {
            if (model.id == mpid) {
                item = model;
                break;
            }
        }
        return item;
    }

    // 现在判断是否播报过，忽略wayid参数
    public boolean isAudioPlayedToday(String resid, int wid) {
        return isAudioPlayed(resid, wid, 60 * 60 * 1000L * 12);
    }

    // 现在判断是否播报过，忽略wayid参数
    public boolean isAudioPlayed(String resid, int wid, long during) {
        String playKey = playedKey(resid, wid);
        long defaulttime = 0;
        long playtime = cacheManager.getMMLong(playKey, defaulttime);
        return playtime != 0 && System.currentTimeMillis() - playtime <= during;
    }

    public void savePlayedTime(String resid, int wid) {
        String key = playedKey(resid, wid);
        cacheManager.setMMLong(key, System.currentTimeMillis());
    }

    public boolean isLastPlayedAudio(String key, long during) {
        long playtime = cacheManager.getMMLong(key, 0L);
        return playtime != 0 && System.currentTimeMillis() - playtime <= during;
    }

    private void initTripLogger(RoadBookModel item, String userid, int itemId) {
        curLogModel = new TripLogModel(item, userid, itemId);
        meetItems = new ArrayList<>();
        playItems = new ArrayList<>();
    }

    private void loadUnfinishedModel() {
        TripLogModel model = (TripLogModel)cacheManager.getMMObject(UnfinishedKey, TripLogModel.class);
        if (model != null) {
            if (model.meetitems != null) {
                meetItems = model.meetitems;
            } else {
                meetItems = new ArrayList<>();
            }
            if (model.playitems != null) {
                ////////////////////////////////////////////////    临时调试用，把所有景点设置为未播放
//                        for (TripLogItem one : model.playitems) {
//                            if (one.id > 0) {
//                                one.status = 0;
//                            }
//                        }
                ////////////////////////////////////////////////    临时调试用
                playItems = model.playitems;
            } else {
                playItems = new ArrayList<>();
            }
            curLogModel = model;
        }
    }

    private void saveModel(boolean forceend) {
        if (isTripBegin()) {
            if (curLogModel != null) {
                curLogModel.meetitems = meetItems;
                curLogModel.playitems = playItems;
                if (forceend && !isTripEnd()) {
                    curLogModel.endtime = System.currentTimeMillis();
                }
                if (curLogModel.wid > 0) {
                    if (isTripEnd()) {
                        if (!isAutoTest) {
                            String jsonstr = new Gson().toJson(curLogModel);
                            cacheManager.setMMString(UnfinishedKey, "");
                            String fileName = curLogModel.filename;
                            if (fileName == null || fileName.equals("")) {
                                fileName = getFileName();
                                curLogModel.filename = fileName;
                            }
                            String filePath = logRootDirectory();
                            FileManager.saveCoverFile(filePath, fileName, jsonstr);
                        }
                        curLogModel = null;
                        meetItems.clear();
                        playItems.clear();
                    } else {
                        if (isAutoTest) return;
                        curLogModel.endtime = 0;
                        cacheManager.setMMObject(UnfinishedKey, curLogModel);
                    }
                }
            }
        }
    }

    private boolean isTripBegin() {
        if (null != curLogModel) {
            boolean isBegin = curLogModel.isTripBegin();
            if (!isBegin && meetItems.size() > 0) {
                curLogModel.isBegin = 1;
            }
            return curLogModel.isTripBegin();
        }
        return false;
    }

    private boolean isTripEnd() {
        return curLogModel != null && curLogModel.endtime > 0;
    }

    public void quitNavi(boolean reportEnd) {
        if (null != curLogModel) {
            if (reportEnd) {
                __reportEnd(true);
            }

            saveModel(false);
            curLogModel = null;
        }
        if (meetItems != null)
            meetItems.clear();
        if (playItems != null)
            playItems.clear();
    }

    public void endTrip() {
        if (null != curLogModel && curLogModel.wid > 0) {
            __reportEnd(false);
            saveModel(true);
        }
        curLogModel = null;
        if (meetItems != null)
            meetItems.clear();
        if (playItems != null)
            playItems.clear();
    }

    private void __reportEnd(boolean isPause) {

        int wid = null != curLogModel ? curLogModel.wid : 0;
        TripCustomEvent event = new TripCustomEvent((isPause ? 7 : 3), wrapper.getUserId());
        event.type = 1;
        event.wayid = wid;
        PostLogManager.getInstance(mContent).reportEvent(event);
    }

    private String getFileName() {
        String userid = wrapper.getUserId();
        String time = TimeUtil.getDateToString(System.currentTimeMillis(), TimeUtil.DATE_FORMAT);
        String fname = String.format("%s%s%s%s", time + "_", curLogModel.wid + "_", userid, ".trip");
        return fname;
    }

    public String getFileName(TripLogModel model) {
        String userid = wrapper.getUserId();
        String time = TimeUtil.getDateToString(System.currentTimeMillis(), TimeUtil.DATE_FORMAT);
        String fname = String.format("%s%s%s%s", time + "_", model.wid + "_", userid, ".trip");
        return fname;
    }

    public String logRootDirectory() {
        if (null == _rootDir) {
            _rootDir = StorageConstants.TRIPCACHE;
        }
        return _rootDir;
    }

    public String playedKey(String resid, int wid) {
        String[] res = resid.split("\\.");
        if (res.length > 0) {
            return "0_" + res[0];
        }
        return wid + "_";
    }

}

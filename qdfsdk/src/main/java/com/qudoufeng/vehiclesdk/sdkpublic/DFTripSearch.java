package com.qudoufeng.vehiclesdk.sdkpublic;

import android.util.SparseArray;

import com.qudoufeng.vehiclesdk.model.GetGpsRouteModel;
import com.qudoufeng.vehiclesdk.model.GpsRouteResp;
import com.qudoufeng.vehiclesdk.model.RoadBookModel;
import com.qudoufeng.vehiclesdk.model.RoadBookResp;
import com.qudoufeng.vehiclesdk.model.tag.AppTagModel;
import com.qudoufeng.vehiclesdk.model.tag.AppTagResp;
import com.qudoufeng.vehiclesdk.sdkpublic.model.DFTagModel;
import com.qudoufeng.vehiclesdk.sdkpublic.model.RoadPointModel;
import com.qudoufeng.vehiclesdk.sdkpublic.model.RoadTripModel;
import com.qudoufeng.vehiclesdk.ui.presenter.RoadBookPresenter;
import com.qudoufeng.vehiclesdk.ui.presenter.TagPresenter;
import com.qudoufeng.vehiclesdk.ui.view.RoadBookViewI;
import com.qudoufeng.vehiclesdk.ui.view.TagViewI;

import java.util.ArrayList;
import java.util.List;

/**
 * 都是数据搜索入口
 */
public class DFTripSearch implements RoadBookViewI, TagViewI {

    /**
     * The interface On trip search listener.
     */
    public interface OnTripSearchListener {
        void onLoadAllTags(List<DFTagModel> taglist);
        void onSearchTripList(List<RoadTripModel> plist);
        void onSearchTripDetail(RoadTripModel tripDetail);
        void onSearchTripPoints(List<RoadPointModel> plist);
        void requestFailed(String msg);
    }

    private OnTripSearchListener listener;
    private RoadBookPresenter detailPresenter = new RoadBookPresenter(this); // 线路详情
    private TagPresenter tagPresenter = new TagPresenter(this);

    /**
     * Instantiates a new Df trip search.
     *
     * @param listener the listener
     */
    public DFTripSearch(OnTripSearchListener listener) {
        this.listener = listener;
    }

    /**
     * 清理监听.
     */
    public void detachListener() {
        detailPresenter.detech();
        tagPresenter.detech();
        this.listener = null;
    }


    /**
     * 获取所有分类标签
     *
     * @param filter 可选参数：all, province, season, feature
     */
    public void fetchAllTags(String filter) {
        tagPresenter.loadAllTags(filter);
    }

    /**
     * 根据标签，获取对应的线路列表
     *
     * @param tagid    the tagid
     * @param location 用户当前位置（没有可以传空字符串），如果有会安装距离排序
     * @param page     当前页数，默认每页10条，暂不支持limit参数
     */
    public void fetchTripList(int tagid, String location, int page) {
        detailPresenter.loadWayList(page, "tag", tagid, location);
    }

    /**
     * 根据线路id，获取线路详情
     *
     * @param wid the wid
     */
    public void fetchTripDetail(int wid) {
        detailPresenter.loadDetail(wid);
    }

    /**
     * 根据线路id，获取线路轨迹
     *
     * @param wid the wid
     */
    public void fetchTripRoute(int wid) {
        detailPresenter.loadViewPoints(wid, 1, 1);
    }



    @Override
    public void loadHomeTags(AppTagResp resp) {
        List<DFTagModel> list = new ArrayList<>();
        if (null != resp) {
            for (AppTagModel one: resp.taglist ) {
                list.add(one.toExportModel());
            }
        }
        listener.onLoadAllTags(list);
    }

    @Override
    public void loadAllTags(AppTagResp resp) {
        List<DFTagModel> taglist = new ArrayList<>();

        if (null != resp && null != resp.taglist) {
            SparseArray<List<DFTagModel>> map = new SparseArray<>();
            for (AppTagModel model : resp.taglist) {
                List<DFTagModel> list = map.get(model.parentid);
                if (null == list) {
                    list = new ArrayList<>();
                    map.put(model.parentid, list);
                }
                list.add(model.toExportModel());
            }
            for (int i=0; i<map.size(); i++) {
                List<DFTagModel> list = map.valueAt(i);

                DFTagModel parentItem = new DFTagModel();
                parentItem.name = list.get(0).parentname;
                parentItem.subtags = list;
                taglist.add(parentItem);
            }
        }

        listener.onLoadAllTags(taglist);
    }

    @Override
    public void loadWayList(RoadBookResp resp) {
        List<RoadTripModel> list = new ArrayList<>();
        for (RoadBookModel one: resp.waylist ) {
            list.add(one.toExportModel());
        }
        listener.onSearchTripList(list);
    }

    @Override
    public void loadDetail(RoadBookResp resp) {
        listener.onSearchTripDetail(resp.way.toExportModel());
    }

    @Override
    public void loadViewPoints(GpsRouteResp resp) {
        List<RoadPointModel> list = new ArrayList<>();
        for (GetGpsRouteModel one: resp.pointlist ) {
            list.add(one.toExportModel());
        }
        listener.onSearchTripPoints(list);
    }

    @Override
    public void requestFailed(String msg) {
        listener.requestFailed(msg);
    }

}

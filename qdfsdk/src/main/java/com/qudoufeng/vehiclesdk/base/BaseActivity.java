package com.qudoufeng.vehiclesdk.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.qudoufeng.vehiclesdk.DFSdk;
import com.qudoufeng.vehiclesdk.UserWrapper;
import com.qudoufeng.vehiclesdk.http.CommonApi;
import com.qudoufeng.vehiclesdk.service.NetBroadcastReceiver;
import com.qudoufeng.vehiclesdk.utils.AppConstants;
import com.qudoufeng.vehiclesdk.utils.ToastUtils;
import com.qudoufeng.vehiclesdk.utils.Utils;

import javax.inject.Inject;

;

/**
 * Created by WanYang on 2018/7/4/004.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetChangeListener {

    @Inject
    protected UserWrapper wrapper;

    @Inject
    protected CommonApi api;

    protected int width = 0, height = 0;

    private NetBroadcastReceiver receiver;
    public static NetBroadcastReceiver.NetChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        listener = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        receiver = new NetBroadcastReceiver();
        registerReceiver(receiver, filter);

        setContentView(getLayoutId());
        DFSdk.component().inject(this);
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction() + "";
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }


        initWidthAndHeight();
        initViewLayout(savedInstanceState);
    }

    protected void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        listener = null;
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    public void copy(String text) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (null != cm) {
            cm.setPrimaryClip(ClipData.newPlainText("text", text));
            shortToast("复制成功！");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        onPageEnd(this.getLocalClassName());
    }

    /**
     * 页面记录
     *
     * @param pageName
     */
    protected void onPageStart(String pageName) {
    }

    /**
     * 页面记录
     *
     * @param pageName
     */
    protected void onPageEnd(String pageName) {
    }

    public boolean isDebug() {
        return DFSdk.isDebug();
    }

    public Bitmap convertViewToBitmap(View view) {
        try {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            Bitmap bitmap = view.getDrawingCache();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract int getLayoutId();

    public abstract void initViewLayout(Bundle savedInstanceState);

    public void shortToast(String toast) {
        if (toast != null)
            ToastUtils.showShort(this, toast);
    }

    public void longToast(String toast) {
        if (toast != null)
            ToastUtils.showLong(this, toast);
    }


    /**
     * 强制关闭软键盘
     *
     * @param v
     */
    public void hiddenKeyBords(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    private void initWidthAndHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;  //屏幕宽
        height = dm.heightPixels;
    }


    protected boolean checkNaviAvailable() {
        if (Utils.isAvilible(this, AppConstants.BaiDuMap) ||
                Utils.isAvilible(this, AppConstants.GoogleMap) ||
                Utils.isAvilible(this, AppConstants.AutoNaviMap)) {
            return true;
        } else {
            shortToast("您没有安装任何地图工具");
        }
        return false;
    }

    @Override
    public void onNetChangeListener(int status) {

    }

}

package com.qudoufeng.vehiclesdk.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.qudoufeng.vehiclesdk.DFSdk;
import com.qudoufeng.vehiclesdk.UserWrapper;
import com.qudoufeng.vehiclesdk.http.CommonApi;

import javax.inject.Inject;


/**
 * Created by WanYang on 2018/7/10/010.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 */

public class BaseFragment extends Fragment {
    @Inject
    protected UserWrapper wrapper;

    @Inject
    protected CommonApi api;

    protected int width, height;

    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = getUserVisibleHint();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DFSdk.component().inject(this);
        initWidthAndHeight();
    }

    private void initWidthAndHeight() {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            FragmentActivity activity = getActivity();
            if (null != activity) {
                WindowManager manager = activity.getWindowManager();
                if (null != manager) {
                    manager.getDefaultDisplay().getMetrics(dm);
                    width = dm.widthPixels;  //屏幕宽
                    height = dm.heightPixels;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copy(String text) {
        if (getActivity() != null) {
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (null != cm) {
                cm.setPrimaryClip(ClipData.newPlainText("text", text));
                shortToast("复制成功！");
            }
        }
    }

    public boolean isDebug() {
        return DFSdk.isDebug();
    }

    protected void shortToast(String toast) {
        if (toast != null && getActivity() != null)
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    protected void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
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

}

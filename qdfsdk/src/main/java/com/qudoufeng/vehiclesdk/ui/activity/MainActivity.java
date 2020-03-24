package com.qudoufeng.vehiclesdk.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qudoufeng.vehiclesdk.R;
import com.qudoufeng.vehiclesdk.base.BaseActivity;
import com.qudoufeng.vehiclesdk.manager.CacheManager;
import com.qudoufeng.vehiclesdk.sdkpublic.DFTripSearch;
import com.qudoufeng.vehiclesdk.sdkpublic.model.DFTagModel;
import com.qudoufeng.vehiclesdk.sdkpublic.model.RoadPointModel;
import com.qudoufeng.vehiclesdk.sdkpublic.model.RoadTripModel;
import com.qudoufeng.vehiclesdk.service.DFTourService;
import com.qudoufeng.vehiclesdk.utils.LocationPoint;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class MainActivity extends BaseActivity implements DFTripSearch.OnTripSearchListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 0;
    private TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewLayout(Bundle savedInstanceState) {

        textView = findViewById(R.id.tv_location);
        StringBuilder builder = new StringBuilder();
        builder.append("123456"+"\n");
        builder.append("654323" + "\n");
        textView.setText(builder);

        Button btn1 = findViewById(R.id.btn_test);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloatWindow(false);
            }
        });

        Button btn2 = findViewById(R.id.btn_test2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloatWindow(true);
            }
        });

        Button btn3 = findViewById(R.id.btn_test3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DFTripSearch search = new DFTripSearch(MainActivity.this);
                search.fetchAllTags("all");

                stopService();
            }
        });
    }

    private DFTourService mFloatService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFloatService = ((DFTourService.MyBinder) service).getService();
            mFloatService.startGuideService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            } else {
                startFloatWindow(false);
            }

        } else {
            startFloatWindow(false);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (requestCode == REQUEST_CODE) {
                startFloatWindow(false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Location event) {
        if (null != event) {
            StringBuilder builder = new StringBuilder();
            builder.append("Longitude = " + event.getLongitude() + "\n");
            builder.append("Latitude = " + event.getLatitude() + "\n");
            textView.setText(builder);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
        EventBus.getDefault().unregister(this);
    }


    private void startFloatWindow(boolean hideFloatWin) {
        if (DFTourService.isStarted) {
            Toast.makeText(this, "服务已启动", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hideFloatWin) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1);
                return;
            }
        }

        Intent intent = new Intent(MainActivity.this, DFTourService.class);
        intent.putExtra("isHideView", hideFloatWin);
        intent.putExtra("autoStart", true);

        // 分别是服务启动，和绑定启动
        startService(intent);
        //bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void stopService() {

//        CacheManager cacheManager = wrapper.getCacheManager();
//        cacheManager.setMMStringExpire("testSP", "2341243", 100);
//        String val = cacheManager.getMMStringExpire("testSP", null);
//
//        cacheManager.setMMString("testSP11", "2341243222");
//        String val1 = cacheManager.getMMString("testSP11", null);
//
//        LocationPoint pt = new LocationPoint(111, 2222);
//        cacheManager.setMMObject("testSP11", pt);
//        LocationPoint pt1 = (LocationPoint)cacheManager.getMMObject("testSP11", LocationPoint.class);
//
//        String val2 = cacheManager.getMMStringExpire("testSP", "");

        // 分别是服务停止，和绑定停止
        Intent intent = new Intent(MainActivity.this, DFTourService.class);
        stopService(intent);

//        if (null != mFloatService && DFTourService.isStarted) {
//            mFloatService.stopSelfService();
//        }
//
//        if (null != serviceConnection) {
//            unbindService(serviceConnection);
//        }
    }


    @Override
    public void onLoadAllTags(List<DFTagModel> taglist) {

    }

    @Override
    public void onSearchTripList(List<RoadTripModel> plist) {

    }

    @Override
    public void onSearchTripDetail(RoadTripModel tripDetail) {

    }

    @Override
    public void onSearchTripPoints(List<RoadPointModel> plist) {

    }

    @Override
    public void requestFailed(String msg) {

    }
}

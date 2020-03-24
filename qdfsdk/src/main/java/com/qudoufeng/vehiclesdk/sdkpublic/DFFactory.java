package com.qudoufeng.vehiclesdk.sdkpublic;

import android.content.Context;

import com.qudoufeng.vehiclesdk.DFSdk;

/**
 * The type Df factory.
 */
public class DFFactory {

    /**
     * 获取sdk实例
     *
     * @param context the  application context
     * @return the sdk
     */
    public static IDFSdk getSdk(Context context) {
        return DFSdk.getInstance(context);
    }

}

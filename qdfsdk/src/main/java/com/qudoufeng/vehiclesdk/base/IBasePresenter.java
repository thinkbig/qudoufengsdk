package com.qudoufeng.vehiclesdk.base;



import com.qudoufeng.vehiclesdk.UserWrapper;
import com.qudoufeng.vehiclesdk.UtilityWrapper;
import com.qudoufeng.vehiclesdk.http.CommonApi;
import com.qudoufeng.vehiclesdk.manager.CacheManager;
import com.qudoufeng.vehiclesdk.utils.AppConstants;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by WanYang on 2018/7/9/009.
 * qq:1072112323
 * company:杭州湾心网络科技有限公司
 * 参考 https://medium.com/@patrykpoborca/dagger-2-and-base-classes-generics-and-presenter-injection-7d82053080c
 */

// 带模板带class，dragger不支持注入，因此加一个帮助类 UtilityWrapper
public abstract class IBasePresenter<V extends IBaseView> {

    public UserWrapper wrapper() {
        return utilityWrapper.wrapper;
    }

    public CommonApi api() {
        return utilityWrapper.api;
    }

    public CacheManager cacheMgr() {
        return utilityWrapper.cacheManager;
    }

    private WeakReference<V> mViewRef;
    private V mProxyView;
    private UtilityWrapper utilityWrapper;

    public IBasePresenter() {
        utilityWrapper = new UtilityWrapper();
    }

    @SuppressWarnings("unchecked")
    public void attech(V view) {
        mViewRef = new WeakReference<>(view);
        // 动态代理，防止空指针
        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if (mViewRef == null || mViewRef.get() == null) {
                    return null;
                }
                return method.invoke(mViewRef.get(), objects);
            }
        });
    }

    public V getView() {
        return mProxyView;
    }

    public void detech() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    protected boolean isDebug() {
        return cacheMgr().getSPBoolean(AppConstants.DEBUG, false);
    }

}


package org.ok.http.mode.callback;


import android.os.Handler;
import android.os.Looper;

import org.ok.http.mode.json.JsonUtils;

import java.io.File;

/**
 * description：所有回调最终会走到这里经过解析被call回去，传递过程以String的形式，避免泛型异常
 * 回调之前为子线程，回调之后为主线程
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class HttpCallback<T> implements BaseCallback {

    private Callback.CommonCallback<T> mCallback;
    private Callback.ProgressCallback<T> mProgressCallback;
    private Handler mHandler;

    public HttpCallback(Callback.CommonCallback<T> callback) {
        // 是否在主线程
        mHandler = new Handler(Looper.getMainLooper());

        // 为空不做处理
        if (callback == null) {
            return;
        }

        // 判断是否需要进度
        if (callback instanceof Callback.ProgressCallback) {
            mProgressCallback = (Callback.ProgressCallback<T>) callback;
        } else {
            mCallback = callback;
        }
    }

    // 进度结果
    @Override
    public void onProgress(final long total, final long current) {
        // 进度
        final float progress = current * 1f / total;
        // 需不需要回调
        if (mProgressCallback != null) {
            // 是否在主线程
            if (isMainThread()) {
                mProgressCallback.onProgress(total, current, progress);
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressCallback.onProgress(total, current, progress);
                }
            });
        }
    }

    // 文件结果
    @Override
    public void onSuccess(final String url, final File file) {
        // 需不需要回调
        if (mProgressCallback != null) {
            // 是否在主线程
            if (isMainThread()) {
                mProgressCallback.onSuccess(url, (T) file);
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressCallback.onSuccess(url, (T) file);
                }
            });

        }
    }


    // json结果
    @Override
    public void onSuccess(final String url, final String data) {

        if (mProgressCallback == null && mCallback == null) return;

        T result = null;
        // 解析json 放在子线程能提高效率
        if (mProgressCallback != null) {
            // 拿class
            Class<?> clazz = JsonUtils.analysisInterfaceInfo(mProgressCallback, Callback.ProgressCallback.class.getName());
            // 解析
            result = parse(data, clazz);
        }
        if (mCallback != null) {
            // 拿class
            Class<?> clazz = JsonUtils.analysisInterfaceInfo(mCallback, Callback.CommonCallback.class.getName());
            // 解析
            result = parse(data, clazz);
        }

        // 结果回调放在主线程
        final T finalResult = result;

        if (isMainThread()) {
            callSuccess(url, finalResult);
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callSuccess(url, finalResult);
            }
        });

    }

    // 解析
    private T parse(String data, Class<?> clazz) {
        T result = null;
        // 解析成list
        try {
            result = (T) JsonUtils.gsonToArr(data, clazz);
        } catch (Exception e) {
            //解析list失败后，解析成普通对象
            if (clazz == String.class) {
                // String 不用解析
                result = (T) data;
            } else {
                // 解析为其他对象
                result = (T) JsonUtils.gsonToObj(data, clazz);
            }
        }
        return result;
    }

    // 成功处理
    private void callSuccess(String url, T finalResult) {
        if (mProgressCallback != null) {
            mProgressCallback.onSuccess(url, finalResult);
        }
        if (mCallback != null) {
            mCallback.onSuccess(url, finalResult);
        }
    }

    @Override
    public void onError(final String url, final Throwable e) {
        if (isMainThread()) {
            callError(url, e);
            return;
        }
        // 结果回调放在主线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callError(url, e);
            }
        });
    }

    // 错误
    private void callError(String url, Throwable e) {
        if (mProgressCallback != null) {
            mProgressCallback.onError(url, e);
        }
        if (mCallback != null) {
            mCallback.onError(url, e);
        }
    }


    @Override
    public void onFinal(final String url) {

        if (isMainThread()) {
            callFinal(url);
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callFinal(url);
            }
        });
    }

    // 最终
    private void callFinal(String url) {
        if (mProgressCallback != null) {
            mProgressCallback.onFinal(url);
        }
        if (mCallback != null) {
            mCallback.onFinal(url);
        }
    }

    /**
     * 是否是在主线程
     */
    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    // 主线程执行
    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

}

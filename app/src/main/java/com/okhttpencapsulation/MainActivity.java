package com.okhttpencapsulation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.ok.http.HttpUtils;
import org.ok.http.engin.OkHttpEngin;
import org.ok.http.mode.callback.Callback;
import org.ok.http.mode.decortor.ParamsDecortor;
import org.ok.http.mode.json.FastJsonParse;
import org.ok.log.L;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        L.debug(true);

        // 桥接引擎 可以自定义
        HttpUtils.init(new OkHttpEngin());
        HttpUtils.initParse(new FastJsonParse());

        HttpUtils.with(this)
                .url("http://www.baidu.com")
                .addDecortor(new ParamsDecortor())// 添加装饰类 可以自定义
                .connectTimeout(30)// 超时时间
                .autoResume()// 断点续传
                .addParams("key","value")// 请求参数
                .addHeader("key","value")// 请求头
                .addParams("key","value","type")// 带type的请求 用于post
                .retryOnConnectionFailure(true)// 重试 默认true
                .get(new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String url, String result) {

                    }

                    @Override
                    public void onError(String url, Throwable e) {

                    }

                    @Override
                    public void onFinal(String url) {

                    }
                });
    }
}

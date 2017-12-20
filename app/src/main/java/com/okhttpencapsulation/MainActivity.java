package com.okhttpencapsulation;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.ok.log.L;
import org.ok.permission.Custom;
import org.ok.permission.PermissionUtils;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        L.debug(true);

        PermissionUtils.with(this)
                .permissions(Manifest.permission.CAMERA)
                .request(new Custom() {
                    @Override
                    public void onNext(boolean isSuccess) {
                        L.e(isSuccess);
                    }
                });
    }
}

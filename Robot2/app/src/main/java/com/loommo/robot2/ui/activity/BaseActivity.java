package com.loommo.robot2.ui.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    private Toast mToast;
    protected Handler mHandler = new Handler();
    private ProgressDialog refreshDialog;

    protected static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    protected void showToast(CharSequence text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    Runnable showProgress = new Runnable() {
        @Override
        public void run() {
            if (refreshDialog == null) {
                refreshDialog = new ProgressDialog(BaseActivity.this);
            }
            refreshDialog.setTitle("提示信息");
            refreshDialog.setMessage("获取信息中......");
            refreshDialog.setCancelable(false);
            refreshDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            refreshDialog.show();
            mHandler.postDelayed(hideProgress, 2000);
        }
    };

    Runnable hideProgress = new Runnable() {
        @Override
        public void run() {
            if (refreshDialog != null) {
                refreshDialog.dismiss();
            }
        }
    };
}

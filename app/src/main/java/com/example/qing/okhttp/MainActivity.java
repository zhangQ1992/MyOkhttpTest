package com.example.qing.okhttp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    final static int SUCC = 2;
    final static int FAIL = 0;

    TextView mTextView;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.txt);
        mButton = (Button) findViewById(R.id.btn_view);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getokhttp("http://www.baidu.com");
            }
        });

    }


    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SUCC:
                    String s = (String) msg.obj;
                    mTextView.setText(s);
                    break;
                case FAIL:
                    mTextView.setText("FAIL!");
                    break;
                default:break;
            }
        }
    };



    public void getokhttp(String url){

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://www.baidu.com").build();
                Response response = null;
                try {
                    response  = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Message msg = new Message();
                        msg.what = SUCC;
                        msg.obj = response.body().string();
                        mHandler.sendMessage(msg);
                    }else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

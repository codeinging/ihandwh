package com.xinqi.ihandwh.Atys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

//import com.xinqi.ihandwh.R;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.xinqi.ihandwh.R;

/**
 * Created by syd on 2015/11/12.
 */
public class WelcomeAty extends Activity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //欢迎界面停留时间
    private static final int TIME_DELAY=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        sharedPreferences=getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putBoolean("isfirstin", true);
        editor.commit();
        //到时间后进入主页
        handler.sendEmptyMessageDelayed(0x123, TIME_DELAY);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        AnalyticsConfig.enableEncrypt(true);
        PushAgent.getInstance(this).enable();
        FeedbackAgent agent=new FeedbackAgent(this);
        agent.sync();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //判断是否已经登陆，如果没有登陆，则进入登陆界面
//            UserinfoUtils userinfoUtils =new UserinfoUtils(WelcomeAty.this);
            /*if (!userinfoUtils.get_Login_Status()){
                Toast.makeText(WelcomeAty.this,"请先登录",Toast.LENGTH_LONG).show();
                goAtyLogin();
            }else {
                goHome();//进入程序主界面
            }*/
            goHome();
        }


    };
   /* private void goAtyLogin() {
        Intent intent=new Intent(WelcomeAty.this,Aty_LogIn.class);
        startActivity(intent);
        finish();
    }*/

    private void goHome() {
        Intent intent=new Intent(WelcomeAty.this,HomeActivity.class);
        startActivity(intent);
        finish();//结束当前的activity
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

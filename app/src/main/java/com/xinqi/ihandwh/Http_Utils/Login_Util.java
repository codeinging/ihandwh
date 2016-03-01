package com.xinqi.ihandwh.Http_Utils;

import android.content.Context;
import android.os.Handler;

/**
 * Created by syd on 2015/11/15.
 */
public class Login_Util {
     private  static boolean Threadhas_stop=false;
    static Handler handler;
    private static Context context;
    //登陆是否成功
    static boolean islog_in_Success =false;
    public Login_Util(Context context){
        this.context=context;
    }

}

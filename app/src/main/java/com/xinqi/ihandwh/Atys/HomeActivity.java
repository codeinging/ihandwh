package com.xinqi.ihandwh.Atys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.xinqi.ihandwh.Local_Utils.UserinfoUtils;
import com.xinqi.ihandwh.ui_components.SlidingTabsColorsFragment;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xinqi.ihandwh.R;

import java.util.Date;

//import com.xinqi.ihandwh.R;

/**s
 * Created by syd on 2015/11/12.
 */

public class HomeActivity extends FragmentActivity {
    long pre_click_time;
    android.app.ActionBar actionBar;
    private  SlidingTabsColorsFragment mSlidingTabsHost;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    WifiManager wifiManager;
    public static int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        pos=getIntent().getIntExtra("pos",0);
       // checkNetworkState();
        sharedPreferences=getSharedPreferences(getResources().getString(R.string.app_name),MODE_PRIVATE);
        editor=sharedPreferences.edit();
       wifiManager= (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //getActionBar().setTitle(getResources().getString(R.string.app_name));
        if(savedInstanceState != null)
            return;
        mSlidingTabsHost=new SlidingTabsColorsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.slidingtab_fragment, mSlidingTabsHost);
        transaction.commit();
        //登录帐号统计
        UserinfoUtils usr=new UserinfoUtils(this);
        MobclickAgent.onProfileSignIn(usr.get_LastId());
        PushAgent.getInstance(this).onAppStart();
        UmengUpdateAgent.update(this);
    }
    //实现按两次退出程序
    @Override
    public void onBackPressed() {
        long click_time=new Date().getTime();
        if (click_time-pre_click_time>2000){
            Toast.makeText(HomeActivity.this,"再按一次退出程序！", Toast.LENGTH_SHORT).show();
            //更新时间
            pre_click_time=click_time;
            return;
        }
        //BookSeatsContentPage.isfirstin=true;
//        Log.i("bac", "离开" + BookSeatsContentPage.isfirstin);
        editor.putBoolean("isfirstin", true);
        editor.commit();
        finish();
//        super.onBackPressed();
    }

    // TODO: 2016/2/27 修复无网络也加载座位信息 
    // TODO: 2016/2/27 删除搜索历史提醒，按钮效果 
    private boolean checkNetworkState() {
        boolean flag = false;
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        } else {
            // Intent it = new Intent(this, ViewPager.class);
            // startActivity(it);
            // // isNetworkAvailable();

        }

        return flag;
    }
    int nettype=0;
    /*private void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //ConnectivityManager类
        Class<?> connectivityManagerClass = null;
        //ConnectivityManager类中的字段
        Field connectivityManagerField = null;


        //IConnectivityManager接口
        Class<?> iConnectivityManagerClass = null;
        //IConnectivityManager接口的对象
        Object iConnectivityManagerObject = null;
        //IConnectivityManager接口的对象的方法
        Method setMobileDataEnabledMethod = null;

        try {
            //取得ConnectivityManager类
            connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            //取得ConnectivityManager类中的字段mService
            connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");
            //取消访问私有字段的合法性检查
            //该方法来自java.lang.reflect.AccessibleObject
            connectivityManagerField.setAccessible(true);


            //实例化mService
            //该get()方法来自java.lang.reflect.Field
            //一定要注意该get()方法的参数:
            //它是mService所属类的对象
            //完整例子请参见:
            //http://blog.csdn.net/lfdfhl/article/details/13509839
            iConnectivityManagerObject = connectivityManagerField.get(connectivityManager);
            //得到mService所属接口的Class
            iConnectivityManagerClass = Class.forName(iConnectivityManagerObject.getClass().getName());
            //取得IConnectivityManager接口中的setMobileDataEnabled(boolean)方法
            //该方法来自java.lang.Class.getDeclaredMethod
            setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            //取消访问私有方法的合法性检查
            //该方法来自java.lang.reflect.AccessibleObject
            setMobileDataEnabledMethod.setAccessible(true);
            //调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConnectivityManagerObject,enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }*/
       /* // 移动数据开启和关闭
        public void setMobileDataStatus(Context context,boolean enabled)
        {
            ConnectivityManager conMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

            //ConnectivityManager类

            Class<?> conMgrClass = null;

            //ConnectivityManager类中的字段
            Field iConMgrField = null;
            //IConnectivityManager类的引用
            Object iConMgr = null;
            //IConnectivityManager类
            Class<?> iConMgrClass = null;
            //setMobileDataEnabled方法
            Method setMobileDataEnabledMethod = null;
            try
            {

                //取得ConnectivityManager类
                conMgrClass = Class.forName(conMgr.getClass().getName());
                //取得ConnectivityManager类中的对象Mservice
                iConMgrField = conMgrClass.getDeclaredField("mService");
                //设置mService可访问
                iConMgrField.setAccessible(true);
                //取得mService的实例化类IConnectivityManager
                iConMgr = iConMgrField.get(conMgr);
                //取得IConnectivityManager类
                iConMgrClass = Class.forName(iConMgr.getClass().getName());

                //取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
                setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);

                //设置setMobileDataEnabled方法是否可访问
                setMobileDataEnabledMethod.setAccessible(true);
                //调用setMobileDataEnabled方法
                setMobileDataEnabledMethod.invoke(iConMgr, enabled);

            }

            catch(ClassNotFoundException e)
            {

                e.printStackTrace();
            }
            catch(NoSuchFieldException e)
            {

                e.printStackTrace();
            }

            catch(SecurityException e)
            {
                e.printStackTrace();

            }
            catch(NoSuchMethodException e)

            {
                e.printStackTrace();
            }

            catch(IllegalArgumentException e)
            {

                e.printStackTrace();
            }

            catch(IllegalAccessException e)
            {

                e.printStackTrace();
            }

            catch(InvocationTargetException e)

            {

                e.printStackTrace();

            }

        }*/


        private void setNetwork() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("网络不可用！");
        builder.setSingleChoiceItems(R.array.nettype, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.i("bac","singlechoice which:"+which);
                /**
                 * 1代表WLAN
                 * 2代表移动数据
                 * */
                nettype = which;

            }
        });
        builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("bac","open which:"+nettype);
                if (nettype==0){//打开WlAN
                wifiManager.setWifiEnabled(true);
                }else {//打开移动数据
//                    toggleMobileData(HomeActivity.this,true);
//                    setMobileDataStatus(HomeActivity.this,true);
                Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // return;
               /* Intent intent = new Intent(ViewPager.this, MainActivity.class);
                intent.putExtra("request", 2);
                startActivity(intent);*/
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
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

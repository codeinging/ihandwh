package com.xinqi.ihandwh.ConfigCenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xinqi.ihandwh.Atys.Aty_LogIn;
import com.xinqi.ihandwh.Atys.HomeActivity;
import com.xinqi.ihandwh.Local_Utils.UserinfoUtils;
import com.umeng.analytics.MobclickAgent;
import com.xinqi.ihandwh.R;

//import com.xinqi.ihandwh.R;

/**
 * Created by Presisco on 2015/9/28.
 */
public class ConfigCenterContentPage extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = ConfigCenterContentPage.class.getSimpleName();
    TextView id_tv,orderhistory,bookroute,aboutour;
    Button btnlog_in_out;
    private boolean haslogin=false;
    UserinfoUtils userinfoUtils;
    public static Fragment newInstance() {
        ConfigCenterContentPage fragment = new ConfigCenterContentPage();
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//    }

    /**
     * Fragment初始化入口，用于绘制界面和初始化，代码在return view和View view中间插入
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab03layout, container, false);
        id_tv= (TextView) view.findViewById(R.id.id_tv);
        //id_tv.setOnClickListener(this);
        view.findViewById(R.id.btn_see_order_record).setOnClickListener(this);
        view.findViewById(R.id.btn_see_collect_book_route).setOnClickListener(this);
        btnlog_in_out= (Button) view.findViewById(R.id.btn_log_in_out);
        btnlog_in_out.setOnClickListener(this);
        view.findViewById(R.id.aboutBtn).setOnClickListener(this);
//        aboutour= (TextView) view.findViewById(R.id.aboutBtn);
//        aboutour.setOnClickListener(this);
        view.findViewById(R.id.feedBackBtn).setOnClickListener(this);
        view.findViewById(R.id.getDeviceTokenBtn).setVisibility(View.GONE);
//        view.findViewById(R.id.getDeviceTokenBtn).setOnClickListener(this);
        return view;
    }
    /**
     * 界面绘制完毕，在super.onViewCreated()后加入代码
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //先判断是否登陆
        userinfoUtils=new UserinfoUtils(getActivity());
        haslogin=userinfoUtils.get_Login_Status();
        Log.i("bac",haslogin+"是否登陆");
        if (haslogin){
            btnlog_in_out.setBackgroundResource(R.drawable.btn_logout_bacground);
            btnlog_in_out.setText("退出登录");
            id_tv.setText("学号："+userinfoUtils.get_LastId());
        }else {
            Log.i("bac", haslogin + "是否登陆");
            btnlog_in_out.setBackgroundResource(R.drawable.btn_login_bacground);
            btnlog_in_out.setText("登录");
            id_tv.setText("学号："+"未登录");
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ConfigCenter");
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_log_in_out:
                if (haslogin){
                    Log.i("bac","8888888");
                    logout();
                }else {
                    Intent intent=new Intent(getActivity(), Aty_LogIn.class);
                    intent.putExtra("from",0);
                    startActivity(intent);
                // TODO: 2015/11/25
                /*getActivity().finish();*/
                }
                break;
            case R.id.aboutBtn:
                startActivity(new Intent(getActivity(),SeeAboutOur.class));
                break;
            case R.id.btn_see_order_record:
                if (haslogin) {
                        startActivity(new Intent(getActivity(),SeeOrderSeatHistory.class));
                }else {
                    Log.i("bac","未登录");
                    Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_see_collect_book_route:
                startActivity(new Intent(getActivity(),SeeBookRouteCollect.class));
                break;
            case R.id.feedBackBtn:
                startActivity(new Intent(getActivity(),CustomFeedBackActivity.class));
//                com.umeng.fb.util.Res.setPackageName("com.xinqi.ihandwh");
//                FeedbackAgent agent=new FeedbackAgent(getActivity());
//                agent.startFeedbackActivity();
                break;
            case R.id.getDeviceTokenBtn:
                startActivity(new Intent(getActivity(),DeviceTokenActivity.class));
                break;
            default:break;
        }
//        if (v.getId()==R.id.btn_log_in_out){
////            Log.i("bac","8888888");
//            if (haslogin){
//                Log.i("bac","8888888");
//                logout();
//            }else {
//                Intent intent=new Intent(getActivity(), Aty_LogIn.class);
//                intent.putExtra("from",0);
//                startActivity(intent);
//                // TODO: 2015/11/25
//                /*getActivity().finish();*/
//            }
//        }else if (v.getId()==R.id.aboutBtn){
//
//                startActivity(new Intent(getActivity(),SeeAboutOur.class));
//        }else if (v.getId()==R.id.btn_see_collect_book_route){
//                startActivity(new Intent(getActivity(),SeeBookRouteCollect.class));
//        }
//        else {
//            if (haslogin) {
//                switch (v.getId()) {
//                    case R.id.btn_see_order_record:
//                        startActivity(new Intent(getActivity(),SeeOrderSeatHistory.class));
//                        break;
//                }
//            }else {
//                Log.i("bac","未登录");
//                Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
//            }
//        }
    }
    /**
    * 退出登录
     * */
    private void logout() {
        Log.i("bac", "退出登录");
        new AlertDialog.Builder(getActivity()).setTitle("退出登录").setMessage("确定要退出登录？").setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userinfoUtils.unsave_CureentLogin_Info();
                userinfoUtils.refresh_Login_Status(false);
                dialog.dismiss();
                new AlertDialog.Builder(getActivity()).setTitle("退出登录").setMessage("退出登录成功！").setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       /* FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();*/
                        /*tx.hide(ConfigCenterContentPage.this);
                        tx.show(ConfigCenterContentPage.this);*/

                        //tx.replace(R.id.configue,ConfigCenterContentPage.this);
//                        tx.commit();
                        Intent intent=new Intent(new Intent(getActivity(),HomeActivity.class));
                        intent.putExtra("pos",2);
//                            HomeActivity homeActivity = new HomeActivity();
//                            homeActivity.viewPager.setCurrentItem(2);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).show();
            }
        }).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ConfigCenter");
    }
}

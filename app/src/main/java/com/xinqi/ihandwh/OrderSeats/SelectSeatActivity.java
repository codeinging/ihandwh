package com.xinqi.ihandwh.OrderSeats;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xinqi.ihandwh.ConfigCenter.LoginActivity;
import com.xinqi.ihandwh.HttpService.OrderSeatService.OrderSeatService;
import com.xinqi.ihandwh.Local_Utils.FloorName2ID;
import com.xinqi.ihandwh.Local_Utils.UserinfoUtils;
import com.xinqi.ihandwh.Model.SeatInfo;
//import com.xinqi.ihandwh.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xinqi.ihandwh.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by syd on 2015/11/13.
 */
public class SelectSeatActivity extends AppCompatActivity{
    private ActionBar actionBar;
    /*//楼层数量
    public static final int FLOORS=12;*/
    //座位显示列数
    private static final Integer COLUMN_COUNT=6;
    //预约日期格式
    private static final String DATE_FORMAT="yyyy/MM/dd";
    public static final String ORDERMODE ="ordermode" ;
    public static final String ORDER_SITNUM ="order_sitNum";
    public static final String ORDER_ROOM ="order_room";
    public static final String ORDER_DATE ="order_date";
    public static  String room;
    public static  String sitNum;
    //存储日期
    String mBookDate;
    //Spinner下拉选项
    Spinner spinner;
    //Spinner适配器
    ArrayAdapter<String> arrayAdapter;
    TextView mBookSeatDate;
    TextView nofloorselectedtv;
    int selectfloor_pos;
    String mSelectedFloorName;

    private RecyclerView mSeatRecyclerView;
    private SeatInfoAdapter mSeatInfoAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    SeatInfo[] mDataSet;

    private List<String> floor_item=new ArrayList<>();
    private boolean randomOrder=false;
    Intent intent;

    private TextView mNoSeatWarn;
    private ProgressBar progressBar;
    private TextView refreshtip;

    private String refreshtiptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.activity_book_seat);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("查座预约");
        actionBar.show();
        getBookDate();
        progressBar= (ProgressBar) findViewById(R.id.refreshperroompro);
        refreshtip = (TextView) findViewById(R.id.roomrefreshtips);
        intent=new Intent(SelectSeatActivity.this, Order_Seat_Process.class);
        mBookSeatDate = (TextView) findViewById(R.id.bookDateTextView);
        mBookSeatDate.setText(getResources().getString(R.string.order_time) + mBookDate);

        //选择楼层事件
        spinner = (Spinner) findViewById(R.id.floorSpinner);
        refreshtip.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        refreshtiptext=refreshtip.getText().toString().trim();
        refreshtip.setText("正在加载楼层信息");
        new AsyncTask<Void,Void,Integer>(){
            @Override
            protected Integer doInBackground(Void... params) {
                UserinfoUtils userinfoUtils=new UserinfoUtils(SelectSeatActivity.this);
                try {
                    if (userinfoUtils.get_Login_Status()) {
                        OrderSeatService.testUserInfoIsTrue(userinfoUtils.get_LastId(), userinfoUtils.get_LastPassword());
                        FloorName2ID.setAvailableID(null);
                    //    FloorName2ID.setAvailableID(OrderSeatService.getFool());
                    } else {
                        OrderSeatService.testUserInfoIsTrue("201100800169", "011796");
                        FloorName2ID.setAvailableID(null);
                    //    FloorName2ID.setAvailableID(OrderSeatService.getFool());
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                floor_item=FloorName2ID.getAllFloors();
                return 0;
            }
            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                // Tell the Fragment that the refresh has completed

                //楼层选项内容数组
                arrayAdapter = new ArrayAdapter<String>(SelectSeatActivity.this, android.R.layout.simple_spinner_item, floor_item);
                spinner.setAdapter(arrayAdapter);
                nofloorselectedtv = (TextView) findViewById(R.id.noselectFloortv);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mNoSeatWarn.setVisibility(View.GONE);
                        mSeatInfoAdapter.clearSelection();
                        //选择不限
                        selectfloor_pos = position;
                        mSelectedFloorName = floor_item.get(position);
                        if (position == 0) {
                            randomOrder = true;
                            refreshtip.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            mSeatRecyclerView.setVisibility(View.GONE);
                            nofloorselectedtv.setVisibility(View.VISIBLE);
                        } else {
                            randomOrder = false;
                            mSeatRecyclerView.setVisibility(View.INVISIBLE);
                            nofloorselectedtv.setVisibility(View.GONE);
                            refreshtip.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            //Toast.makeText(SelectSeatActivity.this,getResources().getString(R.string.loading),Toast.LENGTH_LONG).show();
                            new DummyBackgroundTask().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                refreshtip.setVisibility(View.GONE);
                refreshtip.setText(refreshtiptext);
                progressBar.setVisibility(View.GONE);
            }
        }.execute();

        mSeatRecyclerView = (RecyclerView) findViewById(R.id.seatInfoRecyclerView);

        // GridLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mRecyclerViewLayoutManager = new GridLayoutManager(this,COLUMN_COUNT);
        mSeatRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);

        mSeatInfoAdapter = new SeatInfoAdapter(new SeatInfo[0],this);
        mSeatInfoAdapter.setPickColors(getResources().getColor(R.color.default_background_color),
                                        getResources().getColor(R.color.selected_background_color));
        // Set CustomAdapter as the adapter for RecyclerView.
        mSeatRecyclerView.setAdapter(mSeatInfoAdapter);

        findViewById(R.id.btn_order_seats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击预约按钮的处理
              /*  Log.d("SelectSeatActivity","Floor_ID:"+FloorName2ID.getID(mSelectedFloorName)
                        +"|Seat_ID:"+mSeatInfoAdapter.getSelectedSeat().id);
                Toast.makeText(SelectSeatActivity.this,"Floor_ID:"+FloorName2ID.getID(mSelectedFloorName)
                        +"|Seat_ID:"+mSeatInfoAdapter.getSelectedSeat().id,Toast.LENGTH_LONG).show();*/
                UserinfoUtils userinfoUtils = new UserinfoUtils(SelectSeatActivity.this);
                //判断是否登陆
                if (userinfoUtils.get_Login_Status()) {
                    if (randomOrder) {
                        // TODO: 2015/11/18 一键预约
                        Log.i("bacground", "on key order");
                        //0代表一键预约
                        intent.putExtra(SelectSeatActivity.ORDERMODE, 0);
                        startActivity(intent);

                    } else {
                        if (mSeatInfoAdapter.getSelectedSeat() == null) {
                            Toast.makeText(SelectSeatActivity.this, "请选择座位！", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.putExtra(SelectSeatActivity.ORDERMODE, 1);
                            intent.putExtra(SelectSeatActivity.ORDER_ROOM, FloorName2ID.getID(mSelectedFloorName));
                            intent.putExtra(SelectSeatActivity.ORDER_SITNUM, mSeatInfoAdapter.getSelectedSeat().id);
                            intent.putExtra(SelectSeatActivity.ORDER_DATE, mBookDate);
                            startActivity(intent);
                            Log.i("bacground", "specific order" + FloorName2ID.getID(mSelectedFloorName) + "=="
                                    + mSeatInfoAdapter.getSelectedSeat().id + "--"
                                    + mBookDate);
                        }
                    }
                } else {
                    Toast.makeText(SelectSeatActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SelectSeatActivity.this, LoginActivity.class));
                }
            }
        });

     /*   //后退按键
        findViewById(R.id.btnback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        if (getIntent().getBooleanExtra("random",true)) {
            Log.d("SelectSeatActivity","LaunchRandom");
            //spinner选择不限
        } else {
            Log.d("SelectSeatActivity", "LaunchSelected");
            //spinner选择mSelectedFloorName
            mSelectedFloorName=getIntent().getStringExtra("floorname");
            spinner.setSelection(arrayAdapter.getPosition(mSelectedFloorName),true);
            Log.d("SelectSeatActivity","PreSelected:pos="+arrayAdapter.getPosition(mSelectedFloorName)
                    +" name="+mSelectedFloorName);
        }

        mNoSeatWarn=(TextView)findViewById(R.id.noSeatTextView);
        mNoSeatWarn.setVisibility(View.INVISIBLE);

        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onRefreshComplete() {
        if (mDataSet.length < 1) {
            refreshtip.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mNoSeatWarn.setVisibility(View.VISIBLE);
            mSeatRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            refreshtip.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mNoSeatWarn.setVisibility(View.INVISIBLE);
            mSeatRecyclerView.setVisibility(View.VISIBLE);
            mSeatInfoAdapter.updateDataSet(mDataSet);
            mSeatInfoAdapter.notifyDataSetChanged();
        }
        Toast.makeText(SelectSeatActivity.this,getResources().getString(R.string.load_complete),Toast.LENGTH_SHORT).show();
    }

    private class DummyBackgroundTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            try {
                //判断是否登陆
                List<String> seatRaw;
                UserinfoUtils userinfoUtils=new UserinfoUtils(SelectSeatActivity.this);
                if (userinfoUtils.get_Login_Status()){
                OrderSeatService.testUserInfoIsTrue(userinfoUtils.get_LastId(), userinfoUtils.get_LastPassword());
                seatRaw=OrderSeatService.getYuYueInfo(FloorName2ID.getID(mSelectedFloorName), mBookDate);
                }else {
                OrderSeatService.testUserInfoIsTrue("201100800169","011796");
                seatRaw=OrderSeatService.getYuYueInfo(FloorName2ID.getID(mSelectedFloorName),mBookDate);
                }
                //System.out.println(userinfoUtils.get_LastId()+"==="+userinfoUtils.get_LastPassword());
                mDataSet=new SeatInfo[seatRaw.size()];
                for (int j=0;j<seatRaw.size();j++){
                    mDataSet[j]=new SeatInfo();
                    mDataSet[j].id=seatRaw.get(j);
                }
                Log.d("SelectSeatActivity","SeatInfoFetched!Total Count:"+seatRaw.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Return a new random list of cheeses
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            // Tell the Fragment that the refresh has completed
            onRefreshComplete();
        }

    }

    //获取当前时间函数
    private void getBookDate() {
        SimpleDateFormat converter=new SimpleDateFormat(DATE_FORMAT);
/*
        long time=System.currentTimeMillis()+24*60*60*1000;//long now = android.os.SystemClock.uptimeMillis();
        Date d1=new Date(time);
        mBookDate=converter.format(d1);*/

        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        mBookDate=converter.format(calendar.getTime());
        Log.d("getBookDate()",mBookDate);
    }

    @Override
    public void onBackPressed() {
        finish();
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


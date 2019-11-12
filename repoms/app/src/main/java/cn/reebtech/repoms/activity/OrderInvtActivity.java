package cn.reebtech.repoms.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.uhf.reader.UhfReader;
import com.nlscan.android.uhf.TagInfo;
import com.nlscan.android.uhf.UHFManager;
import com.nlscan.android.uhf.UHFReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pda.serialport.Tools;
import cn.reebtech.repoms.R;
import cn.reebtech.repoms.RepomsAPP;
import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.OrderInvtBean;
import cn.reebtech.repoms.bean.OrderRetBean;
import cn.reebtech.repoms.contact.OrderInvtContact;
import cn.reebtech.repoms.contact.OrderRetContact;
import cn.reebtech.repoms.presenter.OrderInvtPresenter;
import cn.reebtech.repoms.presenter.OrderListPresenter;
import cn.reebtech.repoms.presenter.OrderReqPresenter;
import cn.reebtech.repoms.presenter.OrderRetPresenter;
import cn.reebtech.repoms.util.AssetListAdapter;
import cn.reebtech.repoms.util.CommonUtils;
import cn.reebtech.repoms.util.DBUtils;

public class OrderInvtActivity extends BaseActivity<OrderInvtContact.OrderInvtPtr> implements OrderInvtContact.OrderInvtUI,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        AssetListAdapter.OnItemClickListener,
        AssetListAdapter.OnItemLongClickListener {
    private Toolbar toolbar;
    private Typeface iconFont;
    private Spinner spWarehouse;
    private Spinner spInvUsers;
    private ImageButton btnAddAsset;
    private OrderInvtBean record;
    private RecyclerView assetListCon;
    private TextView odate;
    AssetListAdapter rcyAdapter;
    List<String> scanedAssets;
    private String user;
    /*About Read EPC*/
    private final int MSG_FIND_ASSET = 0;
    private final int MSG_NOT_FIND_ASSET = 1;
    private final int MSG_NOT_OPEN_COMMPORT=2;
    private boolean runFlag = false;
    private boolean startFlag = false;
    private UhfReader manager; // UHF manager,UHF Operating handle
    public String epc = "";
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private String[] powers = {"30dbm","26dbm","24dbm","20dbm","18dbm","17dbm","16dbm"};
    private int power = 0 ;//rate of work
    private int area = 0;
    private String warehouse = "";
    /**
     * Handler分发Runnable对象的方式
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FIND_ASSET:
                    Bundle bundle = msg.getData();
                    getPresenter().loadAsset(bundle.getString("rfid"));

                    Log.i("OrderInvtActivity",bundle.getString("rfid"));

                    break;
                case MSG_NOT_FIND_ASSET:
                    break;
                case MSG_NOT_OPEN_COMMPORT:
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_inv);
        user = getIntent().getStringExtra("user");

        initMT90();

        initView();
    }
    private void initView(){
        shared = getSharedPreferences("UhfRfPower", 0);
        editor = shared.edit();
        power = shared.getInt("power", 30);
        area = shared.getInt("area", 2);
        toolbar = findViewById(R.id.tb_order_inv_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.str_title_toolbar_order_inv));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        spWarehouse = (Spinner) findViewById(R.id.sp_warehouse_inv);
        spInvUsers = (Spinner) findViewById(R.id.sp_mgr_inv);
        odate = (TextView) findViewById(R.id.txt_invdate);
        btnAddAsset = (ImageButton) findViewById(R.id.btn_order_inv_add_asset);
        record = new OrderInvtBean();
        assetListCon = (RecyclerView) findViewById(R.id.ryc_order_inv);
        assetListCon.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        assetListCon.addItemDecoration(itemDecoration);
        rcyAdapter = new AssetListAdapter(this);
        assetListCon.setItemAnimator(new DefaultItemAnimator());
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        assetListCon.setHasFixedSize(true);
        assetListCon.setAdapter(rcyAdapter);
        scanedAssets = new ArrayList<String>();
        setListeners();
        getPresenter().initData();
        odate.setText(CommonUtils.parseDateToString(new Date()));
    }
    private void setListeners(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.saveOk:
                        if(startFlag){
                            showToast(getString(R.string.str_tip_save_record_msg));
                        }
                        else{
                            getPresenter().saveOrder(record);
                            getPresenter().initData();
                        }
                        break;
                    case R.id.localList:
                        Intent starterOrderList = new Intent(OrderInvtActivity.this, OrderListActivity.class);
                        starterOrderList.putExtra("type", OrderListPresenter.TYPE_ORDER_INV);
                        startActivity(starterOrderList);
                        break;
                }
                return true;
            }});
        btnAddAsset.setOnClickListener(this);
        rcyAdapter.setOnItemClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_order_inv_add_asset:
                //启用手持机扫描线程
                startOrStopScan();
                break;
        }
    }
    /*
     * 开启扫描线程
     * */
    private void startOrStopScan(){
        if(!runFlag){
            runFlag = true;
            Thread thread = new OrderInvtActivity.AssetScanThread();
            thread.start();
        }
        if (!startFlag) {
            startFlag = true;
            btnAddAsset.setImageDrawable(getResources().getDrawable(R.drawable.icon_scaning_32));
            //Log.i("UHF", "startScan");
            //启动扫描
            startScan();
        } else {
            startFlag = false;
            btnAddAsset.setImageDrawable(getResources().getDrawable(R.drawable.icon_scan_32));
            //Log.i("UHF", "stopScan");
            //停止扫描
            stopScan();
        }
    }
    @Override
    public OrderInvtContact.OrderInvtPtr onBindPresenter() {
        return new OrderInvtPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map=(HashMap<String,String>)parent.getItemAtPosition(position);
        String key=map.get("id");
        if(parent == spWarehouse){
            record.setLocation(key);
        }
        else if(parent == spInvUsers){
            record.setUsermgr(key);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveSuccess() {
        showToast(getString(R.string.str_msg_toast_save_order_success));
        //初始化各信息
        spWarehouse.setSelection(0);
        record.getAssets().clear();
        scanedAssets.clear();
        rcyAdapter.removeAll();
        if(user != null && !user.equals("")){
            for(int i = 0; i < spInvUsers.getAdapter().getCount(); i++){
                Map<String, String> item = (Map<String, String>)spInvUsers.getAdapter().getItem(i);
                if(item.get("id").equals(user)){
                    spInvUsers.setSelection(i);
                }
            }
        }
    }

    @Override
    public void fillSpinner(int type, List<Map<String, String>> data) {
        switch(type){
            case OrderReqPresenter.TYPE_LOAD_DATA_WAREHOUSE:
                spWarehouse.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{ "name", "id"}, new int[] {R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spWarehouse.setOnItemSelectedListener(this);
                warehouse = DBUtils.getBindWarehouse(user);
                //showToast("warehouse:" + warehouse);
                for(int i = 0; i < data.size(); i++){
                    if(data.get(i).get("id").equals(warehouse)){
                        spWarehouse.setSelection(i);
                    }
                }
                break;
            case OrderReqPresenter.TYPE_LOAD_DATA_MGRS:
                spInvUsers.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{ "name", "id"}, new int[] {R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spInvUsers.setOnItemSelectedListener(this);
                String loginusr = RepomsAPP.getUser();
                if(RepomsAPP.getUser() != null && !RepomsAPP.getUser().equals("")){
                    for (int i = 0; i < data.size(); i++) {
                        Map<String, String> item = data.get(i);
                        if(item != null && item.get("id").equals(RepomsAPP.getUser())){
                            spInvUsers.setSelection(i);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void addScanedAsset(AssetBean data) {
        //加入到类表中
        if(record.getAssets() == null){
            record.setAssets(new ArrayList<AssetBean>());
        }
        if(data.getId() != null && !data.getId().equals("")){
            record.getAssets().add(data);
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("num", record.getAssets().size());
            item.put("name", data.getName());
            item.put("id", data.getId() + item.get("num"));
            item.put("asset_code",data.getAsset_code());
            rcyAdapter.addData(rcyAdapter.getItemCount(), item);
        }
    }

    @Override
    public void onClick(View parent, int position) {
        rcyAdapter.removeData(position);
        record.getAssets().remove(position);
        scanedAssets.remove(position);
    }

    @Override
    public boolean onLongClick(View parent, int position) {
        return false;
    }
    @Override
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        manager = UhfReader.getInstance();
        if(manager == null){
            Log.i("openComFailed", "Open ComPort Failed");
            return;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(manager != null){
            manager.setOutputPower(power);
            manager.setWorkArea(area);
        }
    }
    @Override
    protected void onPause(){
        startFlag = false;
        if (manager != null) {
            manager.close();
        }
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        startFlag = false;
        runFlag = false;
        if (manager != null) {
            manager.close();
        }
        unregisterReceiver(mResultReceiver);
        //停止扫描
        stopScan();
        super.onDestroy();
    }

    /**
     * 扫描线程
     */
    class AssetScanThread extends Thread {
        private List<byte[]> epcList;
        byte[] accessPassword = Tools.HexString2Bytes("00000000");
        @Override
        public void run() {
            super.run();
            while (runFlag) {
                if (startFlag) {
                    if(manager!=null){
                        // manager.stopInventoryMulti()
                        epcList = manager.inventoryRealTime(); // inventory real time
                        if (epcList != null && !epcList.isEmpty()) {
                            for (byte[] epc : epcList) {
                                if(epc != null && epc.length > 0) {
                                    String epcStr = Tools.Bytes2HexString(epc, epc.length);
                                    if(!scanedAssets.contains(epcStr)){
                                        scanedAssets.add(epcStr);
                                        Message message = new Message();
                                        message.what = MSG_FIND_ASSET;
                                        Bundle bundle = new Bundle();
                                        bundle.putString("rfid", epcStr);
                                        message.setData(bundle);
                                        mHandler.sendMessage(message);
                                    }
                                    //Log.i("findEPC", epcStr);
                                }
                            }
                        }

                    }
                    epcList = null;
                }
                try{
                    Thread.sleep(50);
                }
                catch(Exception e){}
            }
        }
    }




    private UHFManager mUHFMgr;
    //接收结果
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(!"nlscan.intent.action.uhf.ACTION_RESULT".equals(action))
                return ;
            //标签数据数组
            Parcelable[] tagInfos =  intent.getParcelableArrayExtra("tag_info");
            //本次盘点启动的时间
            long startReading = intent.getLongExtra("extra_start_reading_time", 0l);
            //......
            for(Parcelable parcel : tagInfos)
            {
                TagInfo tagInfo = (TagInfo)parcel;
                String epcStr = UHFReader. bytes_Hexstr(tagInfo. EpcId);
                Log.d("TAG","Epc ID : "+ epcStr);

                if(!scanedAssets.contains(epcStr)){
                    scanedAssets.add(epcStr);
                    Message message = new Message();
                    message.what = MSG_FIND_ASSET;
                    Bundle bundle = new Bundle();
                    bundle.putString("rfid", epcStr);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }

            }
        }//end onReceiver
    };
    /**
     * clc启动扫描
     */
    private void startScan(){
        //----启动盘点
        UHFReader.READER_STATE er = mUHFMgr.startTagInventory();
        if( er == UHFReader.READER_STATE. OK_ERR){
            Log.d("TAG","MT90手持机盘点启动成功");
        }else{
            Log.d("TAG","MT90手持机盘点启动失败");
        }
    }
    private void stopScan(){
        //----停止盘点
        UHFReader.READER_STATE er = mUHFMgr.stopTagInventory();
        if( er == UHFReader.READER_STATE. OK_ERR){
            Log.d("TAG","MT90手持机盘点停止成功");
        }else{
            Log.d("TAG","MT90手持机盘点停止成功");
        }
    }
    /**
     * clc添加
     */
    private void initMT90() {
        mUHFMgr = UHFManager.getInstance();
        //注册广播监听
        IntentFilter iFilter = new IntentFilter("nlscan.intent.action.uhf.ACTION_RESULT");
        registerReceiver(mResultReceiver, iFilter);
    }



}

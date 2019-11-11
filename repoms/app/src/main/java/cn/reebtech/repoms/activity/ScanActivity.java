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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.uhf.reader.UhfReader;
import com.nlscan.android.uhf.TagInfo;
import com.nlscan.android.uhf.UHFManager;
import com.nlscan.android.uhf.UHFReader;

import java.util.List;

import cn.pda.serialport.Tools;
import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.contact.ScanContact;
import cn.reebtech.repoms.presenter.ScanPresenter;

public class ScanActivity extends BaseActivity<ScanContact.ScanPtr> implements ScanContact.ScanUI,  View.OnClickListener {
    private Toolbar toolbar;
    private Typeface iconFont;
    private TextView txtRfid;
    private TextView txtRfidName;
    private TextView txtRfidLocation;
    private TextView txtRfidFact;
    private TextView txtRfidType;
    private TextView txtRfidPrice;
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

                Message message = new Message();
                message.what = MSG_FIND_ASSET;
                Bundle bundle = new Bundle();
                bundle.putString("rfid", epcStr);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }//end onReceiver
    };









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
        setContentView(R.layout.activity_scan);

        initMT90();

        initView();
    }

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
    /**
     * clc添加
     */
    private void initMT90() {
        mUHFMgr = UHFManager.getInstance();
        //注册广播监听
        IntentFilter iFilter = new IntentFilter("nlscan.intent.action.uhf.ACTION_RESULT");
        registerReceiver(mResultReceiver, iFilter);
    }

    private void initView(){
        shared = getSharedPreferences("UhfRfPower", 0);
        editor = shared.edit();
        power = shared.getInt("power", 30);
        area = shared.getInt("area", 2);
        toolbar = findViewById(R.id.tb_scan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        txtRfid = (TextView) findViewById(R.id.txt_rfid_info);
        txtRfidName = (TextView) findViewById(R.id.txt_rfid_asset_name);
        txtRfidLocation = (TextView) findViewById(R.id.txt_rfid_asset_location);
        txtRfidFact = (TextView) findViewById(R.id.txt_rfid_asset_fact);
        txtRfidType = (TextView) findViewById(R.id.txt_rfid_asset_type);
        txtRfidPrice = (TextView) findViewById(R.id.txt_rfid_asset_price);
        setListeners();
    }

    private void setListeners(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.scan:
                        if(runFlag){
                            if(startFlag){
                                startFlag = false;
                            }
                            else{
                                startFlag = true;
                            }
                        }
                        else{
                            runFlag = true;
                            startFlag = true;
                            new AssetScanThread().start();

                            //启动
                            startScan();
                        }
                        break;
                }
                return true;
            }});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public ScanContact.ScanPtr onBindPresenter() {
        return new ScanPresenter(this);
    }

    @Override
    public void loadAssetInfo(AssetBean asset) {
        txtRfid.setText(asset.getRfid());
        if(asset == null || asset.getId() == null || asset.getId().equals("")){
            txtRfidName.setText("无");
            txtRfidLocation.setText("无");
            txtRfidFact.setText("无");
            txtRfidType.setText("无");
            txtRfidPrice.setText("无");
        }
        else{
            txtRfidName.setText(asset.getName());
            txtRfidLocation.setText(asset.getLocation());
            txtRfidFact.setText(asset.getManut());
            txtRfidType.setText(asset.getSpecification());
            txtRfidPrice.setText("" + asset.getPrice());
        }
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
        if(manager != null) {
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
                    if(manager != null){
                        epcList = manager.inventoryRealTime(); // inventory real time
                        if (epcList != null && !epcList.isEmpty()) {
                            for (byte[] epc : epcList) {
                                if(epc != null && epc.length > 0) {
                                    startFlag = false;
                                    String epcStr = Tools.Bytes2HexString(epc, epc.length);
                                    Message message = new Message();
                                    message.what = MSG_FIND_ASSET;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("rfid", epcStr);
                                    message.setData(bundle);
                                    mHandler.sendMessage(message);
                                }
                            }
                        }
                    }else{
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






}

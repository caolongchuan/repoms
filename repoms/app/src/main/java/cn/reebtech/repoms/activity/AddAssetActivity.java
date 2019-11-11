package cn.reebtech.repoms.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pda.serialport.Tools;
import cn.reebtech.repoms.R;
import cn.reebtech.repoms.RepomsAPP;
import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.contact.AddAssetContact;
import cn.reebtech.repoms.presenter.AddAssetPresenter;

public class AddAssetActivity extends BaseActivity<AddAssetContact.AddAssetPtr> implements AddAssetContact.AddAssetUI, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Typeface iconFont;
    private Spinner spAssetClsFst;
    private Spinner spAssetClsScd;
    private Toolbar toolbar;
    private AssetBean record;
    private EditText txtRFID;
    private EditText txtPrice;
    private EditText txtManut;
    private EditText txtSpec;
    private ImageButton btnScan;
    private TextView txtSugLocation;
    List<String> scanedAssets;
    List<Map<String, String>> bindedRFIDs;
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
    private AssetScanThread scanThread = null;
    /**
     * Handler分发Runnable对象的方式
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FIND_ASSET:
                    startFlag = false;
                    Bundle bundle = msg.getData();
                    getPresenter().checkRfidValid(bundle.getString("rfid"));
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
        setContentView(R.layout.activity_asset_add);

        initMT90();

        initView();
    }

    private void initView(){
        shared = getSharedPreferences("UhfRfPower", 0);
        editor = shared.edit();
        power = shared.getInt("power", 30);
        area = shared.getInt("area", 2);
        toolbar = findViewById(R.id.tb_asset_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        spAssetClsFst = (Spinner) findViewById(R.id.sp_asset_clsfst);
        spAssetClsScd = (Spinner) findViewById(R.id.sp_asset_clsscd);
        txtRFID = (EditText) findViewById(R.id.txt_asset_rfid);
        txtRFID.setInputType(InputType.TYPE_NULL);
        txtPrice = (EditText) findViewById(R.id.txt_asset_price);
        txtPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        txtManut = (EditText) findViewById(R.id.txt_asset_fct);
        txtSpec = (EditText) findViewById(R.id.txt_asset_type);
        btnScan = (ImageButton) findViewById(R.id.imgbtn_asset_add_scan);
        txtSugLocation = (TextView) findViewById(R.id.txt_asset_sug_location);
        record = new AssetBean();
        record.setId(String.valueOf(System.currentTimeMillis()));
        scanedAssets = new ArrayList<String>();
        bindedRFIDs = new ArrayList<Map<String, String>>();
        getPresenter().initData(AddAssetPresenter.TYPE_INIT_DATA_ALL, "");
        getPresenter().loadBindedRFIDs();
        setListeners();
    }

    private void setListeners(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.saveOk:
                        //检查数据是否有效
                        if(record.getClsct().equals("") || record.getName().equals("")){
                            Toast.makeText(AddAssetActivity.this, getString(R.string.str_save_asset_clsct_invalid), Toast.LENGTH_LONG);
                            break;
                        }
                        if(txtRFID.getText().toString().equals("") || txtRFID.getText().toString().equals(getString(R.string.str_rfid_binded))){
                            //txtRFID.setText("12312alskdfasd");
                            Toast.makeText(AddAssetActivity.this, getString(R.string.str_save_asset_rfid_invalid), Toast.LENGTH_LONG);
                            break;
                        }
                        record.setManut(txtManut.getText().toString());
                        if(txtPrice.getText().toString().equals("")){
                            record.setPrice(0);
                        }
                        else{
                            record.setPrice(Double.valueOf(txtPrice.getText().toString()));
                        }
                        record.setSpecification(txtSpec.getText().toString());
                        //组合数据并返回
                        record.setRfid(txtRFID.getText().toString());
                        Intent retIntent = getIntent();
                        retIntent.putExtra("asset", record);
                        AddAssetActivity.this.setResult(1000, retIntent);
                        finish();
                        break;
                }
                return true;
            }});
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent = getIntent();
                retIntent.putExtra("asset", new AssetBean());
                AddAssetActivity.this.setResult(1000, retIntent);
                finish();
            }
        });
        btnScan.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_list, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgbtn_asset_add_scan:
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
    }

    @Override
    public AddAssetContact.AddAssetPtr onBindPresenter() {
        return new AddAssetPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map=(HashMap<String,String>)parent.getItemAtPosition(position);
        String key=map.get("id");
        String name = map.get("name");
        if(parent == spAssetClsFst){
            getPresenter().initData(AddAssetPresenter.TYPE_INIT_DATA_CLSSCD, key);
        }
        else if(parent == spAssetClsScd){
            record.setName(name);
            record.setClsct(key);
            //根据小类存放位置信息更新显示存放规定
            getPresenter().loadSugLocation(key);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void saveAsset() {

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
        if(manager!=null){
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
        super.onDestroy();
    }
    @Override
    public void fillSpinner(int type, List<Map<String, String>> data) {
        switch(type){
            case AddAssetPresenter.TYPE_INIT_DATA_CLSFST:
                spAssetClsFst.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{ "name", "id"}, new int[] {R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spAssetClsFst.setOnItemSelectedListener(this);
                break;
            case AddAssetPresenter.TYPE_INIT_DATA_CLSSCD:
                spAssetClsScd.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{ "name", "id"}, new int[] {R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spAssetClsScd.setOnItemSelectedListener(this);
                break;
        }
    }
    @Override
    public void setBindedRFIDs(List<Map<String, String>> rfids) {
        if(rfids != null && rfids.size() > 0){
            this.bindedRFIDs = rfids;
        }
    }
    @Override
    public void updateSugLocation(String location) {
        txtSugLocation.setText(location);
    }

    @Override
    public void setRfidValidResult(Map<String, Object> result) {
        boolean valid = Boolean.parseBoolean(result.get("valid").toString());
        boolean canbind = Boolean.parseBoolean(result.get("canbind").toString());
        String rfid = result.get("rfid").toString();
        if(valid){
            if(canbind){
                txtRFID.setText(rfid);
            }
            else{
                final String rfidi = rfid;
                new AlertDialog.Builder(AddAssetActivity.this).setTitle(getString(R.string.str_confirm_rfid_rebind))
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtRFID.setText(rfidi);
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                                //Toast.makeText(MainActivity.this, "你点击了返回键", Toast.LENGTH_LONG).show();
                            }
                        }).show();
            }
        }
        else{
            showToast(getString(R.string.str_rfid_not_valid_need_upload));
        }
    }

    private boolean rfidValid(String rfid){
        boolean result = true;
        for(Map<String, String> item : bindedRFIDs){
            if(item.get("rfid").equals(rfid)){
                result = false;
                break;
            }
        }
        return result;
    }
    @Override
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG);
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
                    if(manager!= null){
                        // manager.stopInventoryMulti()
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
            assert tagInfos != null;
            for(Parcelable parcel : tagInfos)
            {
                TagInfo tagInfo = (TagInfo)parcel;
                String epcStr = UHFReader. bytes_Hexstr(tagInfo. EpcId);
                Log.d("TAG","Epc ID : "+ epcStr);

                startFlag = false;
                Message message = new Message();
                message.what = MSG_FIND_ASSET;
                Bundle bundle = new Bundle();
                bundle.putString("rfid", epcStr);
                message.setData(bundle);
                mHandler.sendMessage(message);

                stopScan();
                break;
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

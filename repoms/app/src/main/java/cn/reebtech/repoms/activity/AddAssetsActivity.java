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
import android.text.TextWatcher;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pda.serialport.Tools;
import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.contact.AddAssetsContact;
import cn.reebtech.repoms.presenter.AddAssetPresenter;
import cn.reebtech.repoms.presenter.AddAssetsPresenter;

public class AddAssetsActivity extends BaseActivity<AddAssetsContact.AddAssetsPtr>
        implements AddAssetsContact.AddAssetsUI, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Typeface iconFont;
    private Spinner spAssetsClsFst;
    private Spinner spAssetsClsScd;
    private Toolbar toolbar;
    private AssetBean record;
    //private List<AssetBean> records;
    private EditText txtScanCount;
    private ImageButton btnScan;
    private TextView txtSugLocation;
    private List<String> scanedAssets;
    private List<String> bindedRFIDs;
    //List<Map<String, String>> bindedRFIDs;
    /*About Read EPC*/
    private final int MSG_FIND_ASSET = 0;
    private final int MSG_NOT_FIND_ASSET = 1;
    private final int MSG_NOT_OPEN_COMMPORT = 2;
    private boolean runFlag = false;
    private boolean startFlag = false;
    private UhfReader manager; // UHF manager,UHF Operating handle
    public String epc = "";
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private String[] powers = {"30dbm", "26dbm", "24dbm", "20dbm", "18dbm", "17dbm", "16dbm"};
    private int power = 0;//rate of work
    private int area = 0;
    private AssetScanThread scanThread = null;
    private String warehouse;

    private EditText mManut;
    private EditText mPrice;
    private EditText mCZL;

    private int asset_code_ = 0;
    /**
     * Handler分发Runnable对象的方式
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FIND_ASSET:
                    Bundle bundle = msg.getData();
                    //getPresenter().checkRfidValid(bundle.getString("rfid"));
                    addScanedAsset(bundle.getString("rfid"));

                    Log.i("AddAssetsActivity", "handleMessage: " + bundle.getString("rfid"));

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
        setContentView(R.layout.activity_assets_add);

        initMT90();

        initView();
    }

    private void initView() {
        shared = getSharedPreferences("UhfRfPower", 0);
        editor = shared.edit();
        power = shared.getInt("power", 30);
        area = shared.getInt("area", 2);
        toolbar = findViewById(R.id.tb_asset_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        spAssetsClsFst = (Spinner) findViewById(R.id.sp_assets_clsfst);
        spAssetsClsScd = (Spinner) findViewById(R.id.sp_assets_clsscd);
        txtScanCount = (EditText) findViewById(R.id.txt_assets_count);
        txtScanCount.setInputType(InputType.TYPE_NULL);
        txtScanCount.setText("0");
        btnScan = (ImageButton) findViewById(R.id.imgbtn_assets_add_scan);
        txtSugLocation = (TextView) findViewById(R.id.txt_assets_sug_location);
        record = new AssetBean();
        record.setId(String.valueOf(System.currentTimeMillis()));
        //records = new ArrayList<AssetBean>();
        scanedAssets = new ArrayList<String>();
        bindedRFIDs = new ArrayList<String>();
        warehouse = getIntent().getStringExtra("warehouse");
        getPresenter().initData(AddAssetPresenter.TYPE_INIT_DATA_ALL, "", warehouse);
        getPresenter().loadBindedRFIDs();
        bindedRFIDs.addAll(getIntent().getStringArrayListExtra("scanedrfids"));
        //showToast(warehouse);
        mManut = findViewById(R.id.txt_asset_fct);
        mPrice = findViewById(R.id.txt_asset_price);
        mCZL = findViewById(R.id.txt_asset_czl);
        setListeners();
    }

    private void setListeners() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.saveOk:
                        boolean b = false;
                        try {
                            if (!mPrice.getText().toString().equals(""))
                                Double.parseDouble(mPrice.getText().toString());
                            if (!mCZL.getText().toString().equals(""))
                                Integer.parseInt(mCZL.getText().toString());
                            b = true;
                        } catch (NumberFormatException ignored) {
                        }
                        if (!b) {
                            Toast.makeText(AddAssetsActivity.this, "请检查数据格式是否正确", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (startFlag) {
                            showToast(getString(R.string.str_tip_save_record_msg));
                            break;
                        }
                        //检查数据是否有效
                        if (record.getClsct().equals("") || record.getName().equals("")) {
                            showToast(getString(R.string.str_save_asset_clsct_invalid));
                            break;
                        }
                        if (txtScanCount.getText().toString().equals("") || txtScanCount.getText().toString().equals("0")) {
                            //txtRFID.setText("12312alskdfasd");
                            showToast(getString(R.string.str_save_asset_rfid_invalid));
                            break;
                        }
                        //组合数据并返回
                        Intent retIntent = getIntent();
                        Bundle bundle = new Bundle();
                        List<AssetBean> records = new ArrayList<AssetBean>();
                        for (String rfid : scanedAssets) {
                            AssetBean item = new AssetBean();
                            item.setClsct(record.getClsct());
                            item.setName(record.getName());
                            item.setLocation(record.getLocation());
                            if(mPrice.getText().toString().equals("")){
                                item.setPrice(0);
                            }else{
                                item.setPrice(Double.valueOf(mPrice.getText().toString()));
                            }
                            String manut = mManut.getText().toString();
                            if (manut.equals("")) {
                                item.setManut("0");
                            } else {
                                item.setManut(mManut.getText().toString());
                            }
                            item.setSpecification("");
                            item.setRfid(rfid);
                            item.setAsset_code(GenerateAssetCode(null, rfid)); //clc设置资产编码////////////////////////////////////////////////////////////////////
                            String czl = mCZL.getText().toString();
                            if (czl.equals("")) {
                                item.setCzl("0");
                            } else {
                                item.setCzl(czl);
                            }
                            records.add(item);
                        }
                        bundle.putSerializable("assets", (Serializable) records);
                        //retIntent.putExtra("assets", record);
                        retIntent.putExtras(bundle);
                        AddAssetsActivity.this.setResult(1001, retIntent);
                        finish();
                        break;
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("assets", new ArrayList<AssetBean>());
                retIntent.putExtras(bundle);
                AddAssetsActivity.this.setResult(1001, retIntent);
                finish();
            }
        });
        btnScan.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        Intent retIntent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("assets", new ArrayList<AssetBean>());
        retIntent.putExtras(bundle);
        AddAssetsActivity.this.setResult(1001, retIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_list, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_assets_add_scan:
                //启用手持机扫描线程
                startOrStopScan();
                break;
        }
    }

    /*
     * 开启扫描线程
     * */
    private void startOrStopScan() {
        if (!runFlag) {
            runFlag = true;
            Thread thread = new AssetScanThread();
            thread.start();
        }
        if (!startFlag) {
            startFlag = true;
            btnScan.setImageDrawable(getResources().getDrawable(R.drawable.icon_scaning_32));
            Log.i("UHF", "startScan");
            //启动
            startScan();
        } else {
            startFlag = false;
            btnScan.setImageDrawable(getResources().getDrawable(R.drawable.icon_scan_32));
            Log.i("UHF", "stopScan");
            //停止
            stopScan();
        }
    }

    @Override
    public AddAssetsContact.AddAssetsPtr onBindPresenter() {
        return new AddAssetsPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
        String key = map.get("id");
        String name = map.get("name");
        if (parent == spAssetsClsFst) {
            getPresenter().initData(AddAssetPresenter.TYPE_INIT_DATA_CLSSCD, key, warehouse);
        } else if (parent == spAssetsClsScd) {
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
    protected void onResume() {
        super.onResume();
        manager = UhfReader.getInstance();
        if (manager == null) {
            Log.i("openComFailed", "Open ComPort Failed");
            return;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (manager != null) {
            manager.setOutputPower(power);
            manager.setWorkArea(area);
        }
    }

    @Override
    protected void onPause() {
        startFlag = false;
        if (manager != null) {
            manager.close();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        startFlag = false;
        runFlag = false;
        if (manager != null) {
            manager.close();
        }
        unregisterReceiver(mResultReceiver);
        stopScan();
        super.onDestroy();
    }

    @Override
    public void saveAssets() {

    }

    private void addScanedAsset(String rfid) {
        int scount = Integer.valueOf(txtScanCount.getText().toString()).intValue() + 1;
        txtScanCount.setText(String.valueOf(scount));
    }

    @Override
    public void fillSpinner(int type, List<Map<String, String>> data) {
        switch (type) {
            case AddAssetPresenter.TYPE_INIT_DATA_CLSFST:
                spAssetsClsFst.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spAssetsClsFst.setOnItemSelectedListener(this);
                //showToast(type + ":" + data.size());
                break;
            case AddAssetPresenter.TYPE_INIT_DATA_CLSSCD:
                spAssetsClsScd.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spAssetsClsScd.setOnItemSelectedListener(this);
                break;
        }
    }

    @Override
    public void setBindedRFIDs(List<String> rfids) {
        if (rfids != null && rfids.size() > 0) {
            this.bindedRFIDs.addAll(rfids);
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
        if (valid) {
            if (canbind) {
                txtScanCount.setText(rfid);
            } else {
                final String rfidi = rfid;
                new AlertDialog.Builder(AddAssetsActivity.this).setTitle(getString(R.string.str_confirm_rfid_rebind))
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtScanCount.setText(rfidi);
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
        } else {
            showToast(getString(R.string.str_rfid_not_valid_need_upload));
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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
                    // manager.stopInventoryMulti()
                    if (manager != null) {
                        epcList = manager.inventoryRealTime(); // inventory real time
                        if (epcList != null && !epcList.isEmpty()) {
                            for (byte[] epc : epcList) {
                                if (epc != null && epc.length > 0) {
                                    String epcStr = Tools.Bytes2HexString(epc, epc.length);
                                    if (!scanedAssets.contains(epcStr) && !bindedRFIDs.contains(epcStr)) {
                                        scanedAssets.add(epcStr);
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
                    }
                    epcList = null;
                }
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }
            }
        }
    }


    /**
     * 根据指定规则生成资产编码
     *
     * @param cls
     * @param rfid
     * @return
     */
    private String GenerateAssetCode(String cls, String rfid) {
        long date = new Date().getTime();
        String s = String.valueOf(date+asset_code_ );
        asset_code_++;
        return "asset_code" + s;
    }


    private UHFManager mUHFMgr;
    //接收结果
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!"nlscan.intent.action.uhf.ACTION_RESULT".equals(action))
                return;
            //标签数据数组
            Parcelable[] tagInfos = intent.getParcelableArrayExtra("tag_info");
            //本次盘点启动的时间
            long startReading = intent.getLongExtra("extra_start_reading_time", 0l);
            //......
            for (Parcelable parcel : tagInfos) {
                TagInfo tagInfo = (TagInfo) parcel;
                String epcStr = UHFReader.bytes_Hexstr(tagInfo.EpcId);
                Log.d("TAG", "Epc ID : " + epcStr);

                if (!scanedAssets.contains(epcStr) && !bindedRFIDs.contains(epcStr)) {
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
    private void startScan() {
        //----启动盘点
        UHFReader.READER_STATE er = mUHFMgr.startTagInventory();
        if (er == UHFReader.READER_STATE.OK_ERR) {
            Log.d("TAG", "MT90手持机盘点启动成功");
        } else {
            Log.d("TAG", "MT90手持机盘点启动失败");
        }
    }

    private void stopScan() {
        //----停止盘点
        UHFReader.READER_STATE er = mUHFMgr.stopTagInventory();
        if (er == UHFReader.READER_STATE.OK_ERR) {
            Log.d("TAG", "MT90手持机盘点停止成功");
        } else {
            Log.d("TAG", "MT90手持机盘点停止成功");
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

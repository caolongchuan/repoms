package cn.reebtech.repoms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.Adapter.AssetListAdapter;
import cn.reebtech.repoms.Adapter.BindAssetListAdapter;
import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LabelBendingBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.LabelBendingContact;
import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.WaitBindAssets;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.WaitBindAssetsDao;
import cn.reebtech.repoms.presenter.AddAssetPresenter;
import cn.reebtech.repoms.presenter.DataSyncPresenter;
import cn.reebtech.repoms.presenter.LabelBendingPresenter;
import cn.reebtech.repoms.presenter.OrderListPresenter;
import cn.reebtech.repoms.util.CommonUtils;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.util.SharedPreferencesUtils;
import cn.reebtech.repoms.util.net.ApiService;
import cn.reebtech.repoms.util.net.Contacts;
import cn.reebtech.repoms.util.net.http.RetrofitClient;
import cn.reebtech.repoms.util.net.result.LoginResult;
import cn.reebtech.repoms.util.net.result.UpdataAssetResult;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LabelBindingActivity extends BaseActivity<LabelBendingContact.LabelBendingPtr> implements LabelBendingContact.LabelBendingUI,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        BindAssetListAdapter.OnItemClickListener {

    private static final int MSG_SCAN_RFID = 1;
    private static final int MSG_CHOCE_RFID = 2;
    private Toolbar toolbar;
    private Spinner spBGS;//办公室
    private Spinner spAssetClsFst;
    private Spinner spAssetClsScd;
    private TextView tv_rfid;
    private ImageButton ib_scan;
    private EditText et_assetCode;
    private AssetBean record;
    LabelBendingBean label_bend_record;
    private RecyclerView assetListCon;
    BindAssetListAdapter rcyAdapter;
    private String user;
    private boolean runFlag = false;
    private boolean startFlag = false;

    private String currentClsName = "";

    private List<AssetClsct> asset_clsct_list;
    private List<Assets> all_asset_list;
    private List<Assets> asset_list;

    private List<String> rfid_bending;

    private ApiService apiService;

    private String cut_bgs="";

    /**
     * Handler分发Runnable对象的方式
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SCAN_RFID:
                    Bundle bundle = msg.getData();
                    tv_rfid.setText(bundle.getString("rfid"));
                    //Log.i("find rfid:", bundle.getString("rfid"));
                    break;
                case MSG_CHOCE_RFID:
                    final String rfid = tv_rfid.getText().toString();
                    Bundle bundle1 = msg.getData();
                    final String asset_code = bundle1.getString("asset_code");

                    if (rfid.equals("")) {
                        Toast.makeText(LabelBindingActivity.this, "请先扫描一个标签", Toast.LENGTH_SHORT).show();
                    } else {
                        new AlertDialog.Builder(LabelBindingActivity.this)
                                .setTitle("绑定标签")
                                .setMessage("确定要绑定此标签吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //绑定RFID
                                        bindLabel(rfid, asset_code);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 根据RFID，asset_code绑定
     *
     * @param rfid
     * @param asset_code
     */
    private void bindLabel(final String rfid, final String asset_code) {
/*
        //判断该标签是否已经绑定物资
        for (Assets a : all_asset_list) {
            if (a.getRfid().equals(rfid)) {
                tv_rfid.setText("");
                Toast.makeText(LabelBindingActivity.this, "该标签已经绑定了物资，请换一个标签", Toast.LENGTH_SHORT).show();
                return;
            }
        }
*/
        //正式绑定
        //clc   获取本地保存的token
        SharedPreferencesUtils sp =
                new SharedPreferencesUtils(this, Contacts.GLOBAL_TOKEN);
//        sp.putValues(new SharedPreferencesUtils.ContentValue(Contacts.GLOBAL_TOKEN,token));
        String token = sp.getString(Contacts.GLOBAL_TOKEN);

        if(!token.equals("")){
            JSONArray data = new JSONArray();
            JSONObject subitem = new JSONObject();
            for (Assets a1 : asset_list) {
                if (a1.getAsset_code().equals(asset_code)) {
                    //生成发送的源数据
                    try {
                        subitem.put("rfid", rfid);
                        subitem.put("asset_code", asset_code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    data.put(subitem);
                }
            }
            //发送
            String jsonString = data.toString();
            if (jsonString.equals("[]")) {
//                callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                Toast.makeText(LabelBindingActivity.this, "数据出错", Toast.LENGTH_SHORT).show();
                return;
            }
            RequestBody postData = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
            apiService.featchUpdataRfidAssets(token, postData)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String respResult) {
                            try {
                                JSONObject data = new JSONObject(respResult);
                                if (data.getInt("code") == 200) {
//                                    data.put("type", "aclst");
//                                    callback.onSuccess(DataSyncPresenter.TYPE_DOWN_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                                    //更新界面
                                    for(int i=0;i<asset_list.size();i++){
                                        Assets aa = asset_list.get(i);
                                        String asset_code1 = aa.getAsset_code();
                                        if(asset_code1.equals(asset_code)){
                                            for(int j=0;j<all_asset_list.size();j++){
                                                if(all_asset_list.get(j).getAsset_code().equals(asset_code)){
                                                    all_asset_list.get(j).setRfid(rfid);
                                                    break;
                                                }
                                            }
                                            asset_list.remove(aa);
                                            rcyAdapter.removeDataByAssetCode(asset_code);
                                            //更新数据库
                                            AssetsDao tb_asset = getAssetDao();
                                            Assets load = tb_asset.load(aa.getPid());
                                            load.setRfid(rfid);
                                            tb_asset.update(load);

                                            rfid_bending.add(rfid);
                                            tv_rfid.setText("");
                                            Toast.makeText(LabelBindingActivity.this, "绑定标签成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    AssetsDao tb_asset = getAssetDao();
                                    List<Assets> list = tb_asset.queryBuilder().list();
                                    for(Assets assets:list){
                                            Log.i("clc1","rfid:"+assets.getRfid()+"资产编码："+assets.getAsset_code());
                                    }

                                } else {
                                    Toast.makeText(LabelBindingActivity.this, "绑定标签失败", Toast.LENGTH_SHORT).show();
//                                    callback.onFailure(type, new BaseResultBean(101, "下载物资分类信息失败"));
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
//                            callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                            Toast.makeText(LabelBindingActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            Log.i("complete", "complete");
                        }
                    });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_binding);

        initMT90();

        initView();

        initData();
    }

    private void initData() {
        apiService = RetrofitClient.getInstance(this).provideApiService();

        rfid_bending = new ArrayList<>();
        asset_list = new ArrayList<>();
        AssetsDao tb_asset = getAssetDao();
        all_asset_list = tb_asset.queryBuilder()
                .where(AssetsDao.Properties.Status.eq(1))
                .list();
        for (Assets a : all_asset_list) {
            if (a.getRfid().equals("")) {
                asset_list.add(a);
            }else{
                rfid_bending.add(a.getRfid());
            }
            Log.i("clc1","----rfid:"+a.getRfid()+"----资产编码："+a.getAsset_code());
        }

        AssetClsctDao assetClsctDao = getAssetClsctDao();
        asset_clsct_list = assetClsctDao.queryBuilder().list();
    }

    private void initView() {
        toolbar = findViewById(R.id.tb_asset_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getResources().getString(R.string.str_lbl_label_bending));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        spBGS =  findViewById(R.id.sp_assets_bgs);//办公室
        spAssetClsFst = findViewById(R.id.sp_assets_clsfst);
        spAssetClsScd = findViewById(R.id.sp_assets_clsscd);
        tv_rfid = findViewById(R.id.txt_rfid);
        ib_scan = findViewById(R.id.imgbtn_assets_add_scan);
        ib_scan.setOnClickListener(this);
        et_assetCode = findViewById(R.id.et_search);
        record = new AssetBean();
        label_bend_record = new LabelBendingBean();
        record.setId(String.valueOf(System.currentTimeMillis()));
        assetListCon = (RecyclerView) findViewById(R.id.ryc_order_inv);
        assetListCon.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        assetListCon.addItemDecoration(itemDecoration);
        rcyAdapter = new BindAssetListAdapter(this, this);
        assetListCon.setItemAnimator(new DefaultItemAnimator());
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        assetListCon.setHasFixedSize(true);
        assetListCon.setAdapter(rcyAdapter);

        setListeners();
        getPresenter().initData(LabelBendingPresenter.TYPE_INIT_DATA_ALL, "");
    }

    private void setListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcyAdapter.setOnItemClickListener(this);

        //编辑框文本改变监听
        et_assetCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rcyAdapter.removeAll();
                String asset_code = s.toString();

                if (asset_code.isEmpty()) {
                    spAssetClsFst.setVisibility(View.VISIBLE);
                    spAssetClsScd.setVisibility(View.VISIBLE);

                    for (Assets a : asset_list) {
                        if (currentClsName.equals(a.getClsct())) {
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("name", a.getName());
                            item.put("id", a.getId() + item.get("num"));
                            item.put("asset_code", a.getAsset_code());
                            item.put("clsct", a.getClsct());
                            rcyAdapter.addData(rcyAdapter.getItemCount(), item);
                        }
                    }
                } else {
                    spAssetClsFst.setVisibility(View.INVISIBLE);
                    spAssetClsScd.setVisibility(View.INVISIBLE);

                    for (Assets a : asset_list) {
                        if (a.getAsset_code().contains(asset_code)) {
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("name", a.getName());
                            item.put("id", a.getId() + item.get("num"));
                            item.put("asset_code", a.getAsset_code());
                            item.put("clsct", a.getClsct());
                            rcyAdapter.addData(rcyAdapter.getItemCount(), item);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_assets_add_scan:
                if (runFlag) {
                    startFlag = !startFlag;
                } else {
                    runFlag = true;
                    startFlag = true;
                    //启动
                    startScan();
                }
                break;
        }
    }

    @Override
    public LabelBendingContact.LabelBendingPtr onBindPresenter() {
        return new LabelBendingPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
        String key = map.get("id");
        String name = map.get("name");
        if (parent == spAssetClsFst) {
            getPresenter().initData(LabelBendingPresenter.TYPE_INIT_DATA_CLSSCD, key);
        } else if (parent == spAssetClsScd) {
            record.setName(name);
            record.setClsct(key);
            //根据小类存放位置信息更新显示存放规定
            getPresenter().loadSugLocation(key);
            currentClsName = key;
        } else if(parent == spBGS){
//            getPresenter().initData(LabelBendingPresenter.TYPE_INIT_DATA_BGS,key);
            cut_bgs = key;
            Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
            getPresenter().initData(LabelBendingPresenter.TYPE_INIT_DATA_CLSFST, key);
        }

        rcyAdapter.removeAll();

        for (Assets a : asset_list) {
            if (currentClsName.equals(a.getClsct())) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("name", a.getName());
                item.put("id", a.getId() + item.get("num"));
                item.put("asset_code", a.getAsset_code());
                item.put("clsct", a.getClsct());
                rcyAdapter.addData(rcyAdapter.getItemCount(), item);
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View parent, int position) {

    }

    @Override
    public void onSaveSuccess() {

    }

    @Override
    public void fillSpinner(int type, List<Map<String, String>> data) {
        switch (type) {
            case LabelBendingPresenter.TYPE_INIT_DATA_CLSFST:
                spAssetClsFst.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spAssetClsFst.setOnItemSelectedListener(this);
                break;
            case AddAssetPresenter.TYPE_INIT_DATA_CLSSCD:
                spAssetClsScd.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spAssetClsScd.setOnItemSelectedListener(this);
                break;
            case AddAssetPresenter.TYPE_INIT_DATA_BGS:
                spBGS.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spBGS.setOnItemSelectedListener(this);
                break;
        }
    }

    @Override
    public void addScanedAsset(AssetBean data) {

    }

    private AssetsDao getAssetDao() {
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }

    private AssetClsctDao getAssetClsctDao() {
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }


    /**
     * MT90手持机模块
     */
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

                runFlag = false;
                startFlag = false;

                boolean bendedFlag = false;
                for(String s:rfid_bending){
                    if(s.equals(epcStr)){
                        bendedFlag = true;
                        break;
                    }
                }
                if(!bendedFlag){
                    Message message = new Message();
                    message.what = MSG_SCAN_RFID;
                    Bundle bundle = new Bundle();
                    bundle.putString("rfid", epcStr);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                    stopScan();
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

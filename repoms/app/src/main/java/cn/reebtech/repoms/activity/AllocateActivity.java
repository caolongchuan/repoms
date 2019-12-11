package cn.reebtech.repoms.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.Adapter.AllocateAssetListAdapter;
import cn.reebtech.repoms.Adapter.AssetListAdapter;
import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.contact.AllocateContact;
import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.presenter.AllocatePresenter;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.SharedPreferencesUtils;
import cn.reebtech.repoms.util.net.ApiService;
import cn.reebtech.repoms.util.net.Contacts;
import cn.reebtech.repoms.util.net.http.RetrofitClient;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AllocateActivity extends BaseActivity<AllocateContact.AllocatePtr> implements AllocateContact.AllocateUI,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        AllocateAssetListAdapter.OnItemClickListener {

    private Toolbar toolbar;
    private Spinner spBGS;//办公室
    private Spinner spAssetClsFst;
    private Spinner spAssetClsScd;
    private EditText et_assetCode;
    private AssetBean record;
    private RecyclerView assetListCon;
    AllocateAssetListAdapter rcyAdapter;
    private String user;

    private String currentClsName = "";

    private List<AssetClsct> asset_clsct_list;
    private List<Assets> all_asset_list;
    private List<Assets> asset_list;


    private ApiService apiService;

    private String cut_bgs = "";
    private String new_bgs = "";

    /**
     * Handler分发Runnable对象的方式
     */
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (0x11 == msg.what) {
                Bundle data = msg.getData();
                String asset_code = data.getString("asset_code");

                Allocate(asset_code);
            }
        }
    };

    //根据资产编码进行调拨
    private void Allocate(final String asset_code) {
        AssetsDao tb_asset = getAssetDao();
        Assets assets = tb_asset.queryBuilder()
                .where(AssetsDao.Properties.Asset_code.eq(asset_code))
                .unique();
        AssetClsctDao assetClsctDao = getAssetClsctDao();
        AssetClsct cls_name = assetClsctDao.queryBuilder().where(AssetClsctDao.Properties.Id.eq(assets.getClsct())).unique();

        SharedPreferencesUtils sp =
                new SharedPreferencesUtils(this, Contacts.GLOBAL_TOKEN);
        String token = sp.getString(Contacts.GLOBAL_TOKEN);
        if (!token.equals("")) {
            JSONArray data = new JSONArray();
            JSONObject subitem = new JSONObject();
            //生成发送的源数据
            try {
                subitem.put("asset_code", asset_code);
                subitem.put("old_bgs", cut_bgs);
                subitem.put("new_bgs", new_bgs);
                subitem.put("cls_id",assets.getClsct());
                subitem.put("cls_name",cls_name.getName());
                subitem.put("name",assets.getName());
                subitem.put("user",et_assetCode.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(subitem);

            //发送
            String jsonString = data.toString();
            if (jsonString.equals("[]")) {
//                callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                Toast.makeText(AllocateActivity.this, "数据出错", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody postData = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
            apiService.featchAllocateAssets(token, postData)
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
                                    AssetClsctDao assetClsctDao1 = getAssetClsctDao();

                                    JSONObject result = data.getJSONObject("result");
                                    JSONObject erji = result.getJSONObject("erji");
                                    JSONObject yiji = result.getJSONObject("yiji");

                                    String temp_clsct = "";
                                    String erji_id = erji.getString("id");
                                    if(!erji_id.equals("-1")){
                                        AssetClsct assetClsct = new AssetClsct();
                                        assetClsct.setId(erji.getString("id"));
                                        temp_clsct = erji.getString("id");
                                        assetClsct.setName(erji.getString("title"));
                                        assetClsct.setLayer(0);
                                        assetClsct.setParent(erji.getString("parentId"));
                                        assetClsct.setLocation(erji.getString("whId"));
                                        assetClsctDao1.save(assetClsct);
                                    }
                                    String yiji_id = yiji.getString("id");
                                    if(!yiji_id.equals("-1")){
                                        AssetClsct assetClsct = new AssetClsct();
                                        assetClsct.setId(yiji.getString("id"));
                                        assetClsct.setName(yiji.getString("title"));
                                        assetClsct.setLayer(0);
                                        assetClsct.setParent(yiji.getString("parentId"));
                                        assetClsct.setLocation(yiji.getString("whId"));
                                        assetClsctDao1.save(assetClsct);
                                    }
//                                    data.put("type", "aclst");
//                                    callback.onSuccess(DataSyncPresenter.TYPE_DOWN_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                                    //更新界面
                                    for(int i=0;i<all_asset_list.size();i++){
                                        Assets aa = all_asset_list.get(i);
                                        String asset_code1 = aa.getAsset_code();
                                        if(asset_code1.equals(asset_code)){

                                            all_asset_list.remove(aa);
                                            rcyAdapter.removeDataByAssetCode(asset_code);
                                            //更新数据库
                                            AssetsDao tb_asset = getAssetDao();
                                            Assets load = tb_asset.load(aa.getPid());
                                            load.setLocation(new_bgs);
                                            load.setUser(et_assetCode.getText().toString());
                                            if(!temp_clsct.equals("")){
                                                load.setClsct(temp_clsct);
                                            }

                                            tb_asset.update(load);

                                            Toast.makeText(AllocateActivity.this, "调拨物资成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } else {
                                    Toast.makeText(AllocateActivity.this, "绑定标签失败", Toast.LENGTH_SHORT).show();
//                                    callback.onFailure(type, new BaseResultBean(101, "下载物资分类信息失败"));
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
//                            callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                            Toast.makeText(AllocateActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            Log.i("complete", "complete");
                        }
                    });

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate);

        initView();

        initData();

    }

    private void initView() {
        toolbar = findViewById(R.id.tb_asset_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getResources().getString(R.string.str_menu_icon_allocate));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        spBGS = findViewById(R.id.sp_assets_bgs);//办公室
        spAssetClsFst = findViewById(R.id.sp_assets_clsfst);
        spAssetClsScd = findViewById(R.id.sp_assets_clsscd);
        et_assetCode = findViewById(R.id.et_search);
        record = new AssetBean();
        record.setId(String.valueOf(System.currentTimeMillis()));
        assetListCon = (RecyclerView) findViewById(R.id.ryc_order_inv);
        assetListCon.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        assetListCon.addItemDecoration(itemDecoration);
        rcyAdapter = new AllocateAssetListAdapter(this, this);
        assetListCon.setItemAnimator(new DefaultItemAnimator());
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        assetListCon.setHasFixedSize(true);
        assetListCon.setAdapter(rcyAdapter);

        setListeners();
        getPresenter().initData(AllocatePresenter.TYPE_INIT_DATA_ALL, "");
    }

    private void setListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcyAdapter.setOnItemClickListener(this);

    }

    private void initData() {
        apiService = RetrofitClient.getInstance(this).provideApiService();

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
        String key = map.get("id");
        String name = map.get("name");

        if (parent == spAssetClsFst) {
            new_bgs = key;

        } else if (parent == spAssetClsScd) {
            record.setName(name);
            record.setClsct(key);
            currentClsName = key;


        } else if (parent == spBGS) {
            cut_bgs = key;

            rcyAdapter.removeAll();
            if (all_asset_list != null)
                all_asset_list.clear();
            AssetsDao tb_asset = getAssetDao();
            all_asset_list = tb_asset.queryBuilder()
                    .where(AssetsDao.Properties.Status.eq(1),
                            AssetsDao.Properties.Location.eq(cut_bgs))
                    .list();
            for (Assets a : all_asset_list) {

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
    public AllocateContact.AllocatePtr onBindPresenter() {
        return new AllocatePresenter(this);
    }

    @Override
    public void fillSpinner(int type, List<Map<String, String>> data) {
        switch (type) {
            case AllocatePresenter.TYPE_INIT_DATA_CLSFST:
//                spAssetClsFst.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
//                spAssetClsFst.setOnItemSelectedListener(this);
                break;
            case AllocatePresenter.TYPE_INIT_DATA_CLSSCD:
//                spAssetClsScd.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
//                spAssetClsScd.setOnItemSelectedListener(this);
                break;
            case AllocatePresenter.TYPE_INIT_DATA_BGS:
                spBGS.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spBGS.setOnItemSelectedListener(this);
                spAssetClsFst.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{"name", "id"}, new int[]{R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spAssetClsFst.setOnItemSelectedListener(this);
                break;
        }
    }

    private AssetsDao getAssetDao() {
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }

    private AssetClsctDao getAssetClsctDao() {
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }

    @Override
    public void onClick(View parent, int position) {

    }
}

package cn.reebtech.repoms.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderInBean;
import cn.reebtech.repoms.contact.OrderInContact;
import cn.reebtech.repoms.presenter.OrderInPresenter;
import cn.reebtech.repoms.presenter.OrderListPresenter;
import cn.reebtech.repoms.Adapter.AssetListAdapter;
import cn.reebtech.repoms.util.CommonUtils;
import cn.reebtech.repoms.util.DBUtils;

public class OrderInActivity extends BaseActivity<OrderInContact.OrderInPtr>
        implements OrderInContact.OrderInUI, View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        AssetListAdapter.OnItemClickListener,
        AssetListAdapter.OnItemLongClickListener{
    private Typeface iconFont;
    private Spinner spWarehouse;
    private Spinner spFromUsers;
    private Spinner spMgrs;
    private Toolbar toolbar;
    private TextView odate;
    private ImageButton btnAddAsset;
    private ImageButton btnAddAssets;
    private OrderInBean record;
    private RecyclerView assetListCon;
    AssetListAdapter rcyAdapter;
    private String user;
    private String warehouse = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_in);
        user = getIntent().getStringExtra("user");
        initView();
    }

    private void initView(){
        toolbar = findViewById(R.id.tb_order_in_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.str_title_toolbar_order_in));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        spWarehouse = (Spinner) findViewById(R.id.sp_warehouse);
        spFromUsers = (Spinner) findViewById(R.id.sp_from);
        spMgrs = (Spinner) findViewById(R.id.sp_mgr);
        odate = (TextView) findViewById(R.id.txt_indate);
        btnAddAsset = (ImageButton) findViewById(R.id.btn_order_in_add_asset);
        btnAddAssets = (ImageButton) findViewById(R.id.btn_order_in_add_assets);
        record = new OrderInBean();
        assetListCon = (RecyclerView) findViewById(R.id.ryc_order_in);
        assetListCon.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        assetListCon.addItemDecoration(itemDecoration);
        rcyAdapter = new AssetListAdapter(this);
        assetListCon.setItemAnimator(new DefaultItemAnimator());
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        assetListCon.setHasFixedSize(true);
        assetListCon.setAdapter(rcyAdapter);
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
                        getPresenter().saveOrder(record);
                        getPresenter().initData();
                        break;
                    case R.id.localList:
                        Intent starterOrderList = new Intent(OrderInActivity.this, OrderListActivity.class);
                        starterOrderList.putExtra("type", OrderListPresenter.TYPE_ORDER_IN);
                        startActivity(starterOrderList);
                        break;
                }
                return true;
            }});
        btnAddAsset.setOnClickListener(this);
        btnAddAssets.setOnClickListener(this);
        rcyAdapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_in, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_order_in_add_asset:
                Intent starterAddAsset = new Intent(this, AddAssetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("scanedrfids", (ArrayList<String>) featchScanedRfids());
                bundle.putString("warehouse", warehouse);
                //bundle.putString("test", "test");
                //bundle.putSerializable("asset", null);
                starterAddAsset.putExtras(bundle);
                startActivityForResult(starterAddAsset, 1000);
                break;
            case R.id.btn_order_in_add_assets:
                Intent starterAddAssets = new Intent(this, AddAssetsActivity.class);
                Bundle sbundle = new Bundle();
                //sbundle.putString("test", "test");
                //sbundle.putSerializable("asset", null);
                sbundle.putStringArrayList("scanedrfids", (ArrayList<String>) featchScanedRfids());
                sbundle.putString("warehouse", warehouse);
                //showToast(warehouse);
                starterAddAssets.putExtras(sbundle);
                startActivityForResult(starterAddAssets, 1001);
                break;
        }
    }

    private List<String> featchScanedRfids(){
        List<String> result = new ArrayList<String>();
        if(record.getAssets() != null && record.getAssets().size() > 0)
        for (AssetBean item: record.getAssets()) {
            if(!item.getRfid().equals("")){
                result.add(item.getRfid());
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            AssetBean asset = (AssetBean) data.getSerializableExtra("asset");
            //加入到类表中
            if(record.getAssets() == null){
                record.setAssets(new ArrayList<AssetBean>());
            }
            if(asset.getRfid() != null && !asset.getRfid().equals("")){
                record.getAssets().add(asset);
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("num", record.getAssets().size());
                item.put("name", asset.getName());
                item.put("asset_code", asset.getAsset_code());
                rcyAdapter.addData(rcyAdapter.getItemCount(), item);
            }
        }
        else if(requestCode == 1001){
            //批量添加
            List<AssetBean> assets = (List<AssetBean>) data.getSerializableExtra("assets");
            if(record.getAssets() == null){
                record.setAssets(new ArrayList<AssetBean>());
            }
            if(assets.size() > 0){
                record.getAssets().addAll(assets);
                for(AssetBean asset : assets){
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("num", (rcyAdapter.getItemCount() + 1) + "");
                    item.put("name", asset.getName());
                    item.put("asset_code", asset.getAsset_code());
                    rcyAdapter.addData(rcyAdapter.getItemCount(), item);
                }
            }
        }
    }

    @Override
    public OrderInContact.OrderInPtr onBindPresenter() {
        return new OrderInPresenter(this);
    }

    @Override
    public void onSaveSuccess() {
        //提示保存成功
        showToast(getString(R.string.str_msg_toast_save_order_success));
        //清空数据
        spWarehouse.setSelection(0);
        spFromUsers.setSelection(0);
        record.getAssets().clear();
        rcyAdapter.removeAll();
        if(user != null && !user.equals("")){
            for(int i = 0; i < spMgrs.getAdapter().getCount(); i++){
                Map<String, String> item = (Map<String, String>)spMgrs.getAdapter().getItem(i);
                if(item.get("id").equals(user)){
                    spMgrs.setSelection(i);
                }
            }
        }
    }

    @Override
    public void onSaveFailure(BaseResultBean result) {
        //提示错误信息
        showToast(result.getErrMsg());
    }

    @Override
    public void fillSpinner(int type, List<Map<String, String>> data) {
        switch(type){
            case OrderInPresenter.TYPE_LOAD_DATA_WAREHOUSE:
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
            case OrderInPresenter.TYPE_LOAD_DATA_FROMUSERS:
                spFromUsers.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{ "name", "id"}, new int[] {R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spFromUsers.setOnItemSelectedListener(this);
                break;
            case OrderInPresenter.TYPE_LOAD_DATA_MGRS:
                spMgrs.setAdapter(new SimpleAdapter(this, data, R.layout.simple_spinner_item, new String[]{ "name", "id"}, new int[] {R.id.txt_spinner_item_name, R.id.txt_spinner_item_key}));
                spMgrs.setOnItemSelectedListener(this);
                if(user != null && !user.equals("")){
                    for(int i = 0; i < data.size(); i++){
                        if(data.get(i).get("id").equals(user)){
                            spMgrs.setSelection(i);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map=(HashMap<String,String>)parent.getItemAtPosition(position);
        String key=map.get("id");
        if(parent == spWarehouse){
            record.setLocation(key);
        }
        else if(parent == spFromUsers){
            record.setUsersend(key);
        }
        else if(parent == spMgrs){
            record.setUsermgr(key);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View parent, int position) {
        rcyAdapter.removeData(position);
        record.getAssets().remove(position);
    }

    @Override
    public boolean onLongClick(View parent, int position) {
        return false;
    }

    @Override
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

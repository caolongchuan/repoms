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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.DataSyncBean;
import cn.reebtech.repoms.contact.DataSyncContact;
import cn.reebtech.repoms.presenter.DataSyncPresenter;
import cn.reebtech.repoms.Adapter.SyncItemListAdapter;
import cn.reebtech.repoms.view.LoadingView;

public class DataSyncActivity extends BaseActivity<DataSyncContact.DataSyncPtr>
        implements DataSyncContact.DataSyncUI, View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        SyncItemListAdapter.OnItemClickListener,
        SyncItemListAdapter.OnItemLongClickListener{

    private Typeface iconFont;
    private Toolbar toolbar;
    private DataSyncBean record;
    private RecyclerView rcyDownlItems;
    private RecyclerView rcyUplItems;
    SyncItemListAdapter rcyDownlAdapter;
    SyncItemListAdapter rcyUplAdapter;
    private Button btnDownload;
    private Button btnUpload;

    private LoadingView loadingView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sync);
        initView();

    }
    private void initView(){
        toolbar = findViewById(R.id.tb_data_sync_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getResources().getString(R.string.str_lbl_syn));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        record = new DataSyncBean();
        btnDownload = (Button) findViewById(R.id.btn_data_dl);
        btnUpload = (Button) findViewById(R.id.btn_data_ul);
        rcyDownlItems = (RecyclerView) findViewById(R.id.ryl_items_dl);
        rcyUplItems = (RecyclerView) findViewById(R.id.ryl_items_ul);
        rcyDownlItems.setLayoutManager(new LinearLayoutManager(this));
        rcyUplItems.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcyDownlItems.addItemDecoration(itemDecoration);
        rcyUplItems.addItemDecoration(itemDecoration);
        rcyDownlAdapter = new SyncItemListAdapter(this);
        rcyUplAdapter = new SyncItemListAdapter(this);
        rcyDownlItems.setItemAnimator(new DefaultItemAnimator());
        rcyUplItems.setItemAnimator(new DefaultItemAnimator());
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rcyDownlItems.setHasFixedSize(true);
        rcyDownlItems.setAdapter(rcyDownlAdapter);
        rcyUplItems.setHasFixedSize(true);
        rcyUplItems.setAdapter(rcyUplAdapter);
        setListeners();
        getPresenter().initData();
    }
    /*
    * 设置监听器
    * */
    private void setListeners(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sync_srv_set:
                        Intent serverSetStarter = new Intent(DataSyncActivity.this, ServerSetActivity.class);
                        startActivity(serverSetStarter);
                        break;
                }
                return true;
            }});
        btnDownload.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        rcyDownlAdapter.setOnItemClickListener(this);
        rcyUplAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_data_dl:
                getPresenter().downloadItems(record.getDownlItems());
                break;
            case R.id.btn_data_ul:
                getPresenter().uploadItems(record.getUplItems());
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_sync, menu);
        return true;
    }

    @Override
    public DataSyncContact.DataSyncPtr onBindPresenter() {
        return new DataSyncPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*
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
        */
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRcyItemClick(SyncItemListAdapter parent, View view, int position) {
        if(parent == rcyDownlAdapter){
            if(((CheckBox) view).isChecked()){
                record.getDownlItems().add(Integer.valueOf(position));
            }
            else{
                record.getDownlItems().remove(Integer.valueOf(position));
            }
        }
        else if(parent == rcyUplAdapter){
            if(((CheckBox) view).isChecked()){
                record.getUplItems().add(Integer.valueOf(position));
            }
            else{
                record.getUplItems().remove(Integer.valueOf(position));
            }
        }

        //Log.i("checkStatus:", String.valueOf(((CheckBox) parent).isChecked()));
        //rcyAdapter.removeData(position);
        //record.getAssets().remove(position);
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
    public void onDownloadComplete(BaseResultBean result) {

    }

    @Override
    public void onUploadComplete(BaseResultBean result) {

    }

    @Override
    public void fillRecycleView(int type, List<Map<String, Object>> data) {
        switch(type){
            case DataSyncPresenter.OP_LOAD_DOWNLOAD_ITEMS:
                for(int i = 0; i < data.size(); i++){
                    rcyDownlAdapter.addData(rcyDownlAdapter.getItemCount(), data.get(i));
                }
                break;
            case DataSyncPresenter.OP_LOAD_UPLOAD_ITEMS:
                for(int i = 0; i < data.size(); i++){
                    rcyUplAdapter.addData(rcyUplAdapter.getItemCount(), data.get(i));
                }
                break;
        }
    }
}

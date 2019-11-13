package cn.reebtech.repoms.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.contact.OrderListContact;
import cn.reebtech.repoms.presenter.OrderListPresenter;
import cn.reebtech.repoms.Adapter.OrderListAdapter;

public class OrderListActivity  extends BaseActivity<OrderListContact.OrderListPtr> implements OrderListContact.OrderListUI,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        OrderListAdapter.OnItemClickListener,
        OrderListAdapter.OnItemLongClickListener{
    private Toolbar toolbar;
    private Typeface iconFont;
    private RecyclerView orderListCon;
    private View lytNoRecords;
    OrderListAdapter rcyAdapter;

    private int listType;
    private int opPosition = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Intent intent = getIntent();
        this.listType = intent.getIntExtra("type", 1);
        initView();
    }

    private void initView(){
        toolbar = findViewById(R.id.tb_order_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        switch(listType){
            case OrderListPresenter.TYPE_ORDER_IN:
                toolbar.setTitle(getString(R.string.str_title_toolbar_order_in_list));
                break;
            case OrderListPresenter.TYPE_ORDER_OUT:
                toolbar.setTitle(getString(R.string.str_title_toolbar_order_out_list));
                break;
            case OrderListPresenter.TYPE_ORDER_REQ:
                toolbar.setTitle(getString(R.string.str_title_toolbar_order_req_list));
                break;
            case OrderListPresenter.TYPE_ORDER_RET:
                toolbar.setTitle(getString(R.string.str_title_toolbar_order_ret_list));
                break;
            case OrderListPresenter.TYPE_ORDER_INV:
                toolbar.setTitle(getString(R.string.str_title_toolbar_order_inv_list));
                break;
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        orderListCon = (RecyclerView) findViewById(R.id.rcy_order_list);
        orderListCon.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Drawable drawable = getResources().getDrawable(R.drawable.rcy_item_dec);
        itemDecoration.setDrawable(drawable);
        orderListCon.addItemDecoration(itemDecoration);
        rcyAdapter = new OrderListAdapter(this, listType);
        orderListCon.setItemAnimator(new DefaultItemAnimator());
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        orderListCon.setHasFixedSize(true);
        orderListCon.setAdapter(rcyAdapter);
        lytNoRecords = findViewById(R.id.incd_norecords);
        getPresenter().loadData(this.listType);
        setListeners();
    }

    private void setListeners(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcyAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public OrderListContact.OrderListPtr onBindPresenter() {
        return new OrderListPresenter(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void fillList(List<Map<String, Object>> data) {
        rcyAdapter.setData(data);
        if(data.size() == 0){
            lytNoRecords.setVisibility(View.VISIBLE);
            orderListCon.setVisibility(View.GONE);
        }
        else{
            lytNoRecords.setVisibility(View.GONE);
            orderListCon.setVisibility(View.VISIBLE);
        }
        showToast(getString(R.string.str_tip_load_order_list_complete));
    }

    @Override
    public void onRemoveItemSuccess() {
        if(opPosition >= 0){
            rcyAdapter.removeData(opPosition);
            opPosition = -1;
        }
        if(rcyAdapter.getItemCount() == 0){
            lytNoRecords.setVisibility(View.VISIBLE);
            orderListCon.setVisibility(View.GONE);
        }
        else{
            lytNoRecords.setVisibility(View.GONE);
            orderListCon.setVisibility(View.VISIBLE);
        }
        showToast("记录删除成功");
    }
    @Override
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View parent, final int position) {
        Map<String, Object> item = rcyAdapter.getData(position);
        final String id = String.valueOf(item.get("id"));
        new AlertDialog.Builder(OrderListActivity.this).setTitle("确认要删除这条记录吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrderListActivity.this.opPosition = position;
                        // 点击“确认”后的操作
                        getPresenter().deleteData(OrderListActivity.this.listType, id);
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrderListActivity.this.opPosition = -1;
                        // 点击“返回”后的操作,这里不设置没有任何操作
                        //Toast.makeText(MainActivity.this, "你点击了返回键", Toast.LENGTH_LONG).show();
                    }
                }).show();
    }

    @Override
    public boolean onLongClick(View parent, int position) {
        return false;
    }
}

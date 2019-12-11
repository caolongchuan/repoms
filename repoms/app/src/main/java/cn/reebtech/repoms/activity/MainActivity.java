package cn.reebtech.repoms.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.contact.MainContact;
import cn.reebtech.repoms.presenter.MainPresenter;

public class MainActivity extends BaseActivity<MainContact.MainPtr> implements MainContact.MainUI, View.OnClickListener {
    //变量和控件
    private Typeface iconFont;
    private GridView menuContainer;
    private List<Map<String, Object>> menuList;
    private SimpleAdapter menuAdapter;
    private String user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = getIntent().getStringExtra("user");
        initView();
    }

    protected void initView(){
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        menuContainer = (GridView)findViewById(R.id.grid_menus);
        //加载菜单
        loadMenus();
    }
    /*
    * 加载九宫格系统功能菜单
    * */
    private void loadMenus(){
        //从配置文件读取菜单列表或准备菜单列表
        //图标
        int icon[] = {  R.drawable.icon_menu_order_out, R.drawable.icon_menu_order_ret,
                R.drawable.icon_menu_order_req,R.drawable.icon_menu_order_in,
                R.drawable.icon_menu_inv, R.drawable.icon_menu_scan_32 ,
                R.drawable.icon_menu_sync,R.drawable.icon_menu_label_binding,
                R.drawable.icon_menu_allocate,R.drawable.icon_menu_logout };
        //图标下的文字
        String name[]={getString(R.string.str_menu_order_out),
                getString(R.string.str_menu_order_ret), getString(R.string.str_menu_order_req),
                getString(R.string.str_menu_order_in), getString(R.string.str_menu_inv),
                getString(R.string.str_menu_read_rfid), getString(R.string.str_menu_data_sync),
                getString(R.string.str_menu_label_binding),
                getString(R.string.str_menu_icon_allocate),
                getString(R.string.str_menu_logout)};
        menuList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icon.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("text",name[i]);
            menuList.add(map);
        }
        //声明Adapter
        String[] from = { "img", "text"};
        int[] to = { R.id.img_micon, R.id.txtv_mname};
        menuAdapter=new SimpleAdapter(this, menuList, R.layout.gview_menu_item, from, to);
        menuContainer.setAdapter(menuAdapter);
        //建立Adapter与GridView的绑定
        //为GridView设置单击事件
        menuContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                //builder.setTitle("提示").setMessage(menuList.get(arg2).get("text").toString()).create().show();
                switch(arg2){
                    case 0: //物资出库
                        Intent starterOrderOut = new Intent(MainActivity.this, OrderOutActivity.class);
                        starterOrderOut.putExtra("user", user);
                        startActivity(starterOrderOut);
                        break;
                    case 1: //物资归还
                        Intent starterOrderRet = new Intent(MainActivity.this, OrderRetActivity.class);
                        starterOrderRet.putExtra("user", user);
                        startActivity(starterOrderRet);
                        break;
                    case 2: //物资借用
                        Intent starterOrderReq = new Intent(MainActivity.this, OrderReqActivity.class);
                        starterOrderReq.putExtra("user", user);
                        startActivity(starterOrderReq);
                        break;
                    case 3: //新物资入库
                        Intent starterOrderIn = new Intent(MainActivity.this, OrderInActivity.class);
                        starterOrderIn.putExtra("user", user);
                        startActivity(starterOrderIn);
                        break;
                    case 4: //物资盘点
                        Intent starterOrderInvt = new Intent(MainActivity.this, OrderInvtActivity.class);
                        starterOrderInvt.putExtra("user", user);
                        startActivity(starterOrderInvt);
                        break;
                    case 5://物资识别
                        Intent starterScan = new Intent(MainActivity.this, ScanActivity.class);
                        startActivity(starterScan);
                        break;
                    case 6: //数据同步，有网络时可支持上传下载
                        Intent starterDataSync = new Intent(MainActivity.this, DataSyncActivity.class);
                        startActivity(starterDataSync);
                        break;
                    case 7://绑定单个标签
                        Intent labelBinding = new Intent(MainActivity.this, LabelBindingActivity.class);
                        labelBinding.putExtra("user", user);
                        startActivity(labelBinding);
                        break;
                    case 8://资产调拨
                        Intent allocate = new Intent(MainActivity.this,AllocateActivity.class);
                        allocate.putExtra("user",user);
                        startActivity(allocate);
                        break;
                    case 9: //注销
                        new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.str_title_confirm_dialog_logout))
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton(getString(R.string.str_title_confirm_btn_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 点击“确认”后的操作
                                        getPresenter().logout();
                                    }
                                })
                                .setNegativeButton(getString(R.string.str_title_confirm_btn_cancel), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 点击“返回”后的操作,这里不设置没有任何操作
                                        //Toast.makeText(MainActivity.this, "你点击了返回键", Toast.LENGTH_LONG).show();
                                    }
                                }).show();
                        break;
                    case 10: //临时初始化数据用
                        //getPresenter().tmpInitData(1);
                        //getPresenter().tmpInitData(2);
                        //getPresenter().tmpInitData(3);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public MainContact.MainPtr onBindPresenter() {
        return new MainPresenter(this);
    }


    @Override
    public void logoutSuccess() {
        Toast.makeText(this,"注销成功",Toast.LENGTH_LONG).show();
        Intent starterLogin = new Intent(this, LoginActivity.class);
        startActivity(starterLogin);
        finish();
    }

    @Override
    public void logoutFailuer(BaseResultBean result) {

    }
}

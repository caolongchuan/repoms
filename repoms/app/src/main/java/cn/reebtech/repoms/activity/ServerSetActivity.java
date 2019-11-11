package cn.reebtech.repoms.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.ServerBean;
import cn.reebtech.repoms.contact.ServerSetContact;
import cn.reebtech.repoms.presenter.ServerSetPresenter;

public class ServerSetActivity extends BaseActivity<ServerSetContact.ServerSetPtr> implements ServerSetContact.ServerSetUI, View.OnClickListener  {
    private Typeface iconFont;
    private Toolbar toolbar;
    private EditText txtHost;
    private EditText txtUser;
    private EditText txtPasswd;
    private Button btnTestServer;
    private boolean serverValid;
    private ServerBean server;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_set);
        initView();
    }
    private void initView(){
        toolbar = findViewById(R.id.tb_sysset);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.str_title_toolbar_server_set));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        txtHost = (EditText) findViewById(R.id.txt_server_url);
        txtUser = (EditText) findViewById(R.id.txt_server_user);
        txtPasswd = (EditText) findViewById(R.id.txt_server_passwd);
        btnTestServer = (Button) findViewById(R.id.btn_test_server_url);
        serverValid = false;
        setListeners();
        getPresenter().loadCurrentConfig();
    }
    private void setListeners(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.saveOk:
                        if(serverValid){
                            getPresenter().saveServerSet(server);
                        }
                        else{
                            showToast(getString(R.string.str_err_msg_server_set_test));
                        }
                        break;
                }
                return true;
            }});
        btnTestServer.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_test_server_url:
                if(txtHost.getText().toString().equals("")){
                    showToast(getString(R.string.str_err_msg_server_set_host));
                    break;
                }
                if(txtUser.getText().toString().equals("")){
                    showToast(getString(R.string.str_err_msg_server_set_user));
                    break;
                }
                if(txtPasswd.getText().toString().equals("")){
                    showToast(getString(R.string.str_err_msg_server_set_passwd));
                    break;
                }
                if(server == null){
                    server = new ServerBean();
                }
                server.setHost(txtHost.getText().toString());
                server.setUser(txtUser.getText().toString());
                server.setPasswd(txtPasswd.getText().toString());
                getPresenter().testServerSet(server);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_list,menu);
        return true;
    }
    @Override
    public void showTestResult(BaseResultBean result) {
        setTestStatus(false);
        if(result.getErrCode() == 0){
            serverValid = true;
            showToast(getString(R.string.str_success_msg_server_set));
        }
        else{
            serverValid = false;
            showToast(getString(R.string.str_err_msg_server_set) + ":" + result.getErrMsg());
        }
    }

    @Override
    public void setTestStatus(boolean status){
        btnTestServer.setEnabled(!status);
        txtHost.setEnabled(!status);
        txtUser.setEnabled(!status);
        txtPasswd.setEnabled(!status);
    }

    @Override
    public void showSaveResult(BaseResultBean result) {
        if(result.getErrCode() == 0){
            //关闭当前activity
            showToast(getString(R.string.str_success_msg_server_set_save));
            finish();
        }
        else{
            showToast(getString(R.string.str_err_msg_server_set_save) + result.getErrMsg());
        }
    }

    @Override
    public void showCurrentConfig(ServerBean server) {
        txtHost.setText(server.getHost() == null ? "" : server.getHost());
        txtUser.setText(server.getUser() == null ? "" : server.getUser());
        txtPasswd.setText(server.getPasswd() == null ? "" : server.getPasswd());
    }

    @Override
    public ServerSetContact.ServerSetPtr onBindPresenter() {
        return new ServerSetPresenter(this);
    }
    @Override
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

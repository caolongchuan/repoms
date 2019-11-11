package cn.reebtech.repoms.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import cn.reebtech.repoms.RepomsAPP;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.LoginContact;
import cn.reebtech.repoms.R;
import cn.reebtech.repoms.presenter.LoginPresenter;

public class LoginActivity extends BaseActivity<LoginContact.LoginPtr> implements LoginContact.LoginUI, View.OnClickListener {
    //变量和控件
    private Typeface iconFont;
    private EditText txtUserName;
    private EditText txtUserPassword;
    private CheckBox chkAutoLogin;
    private CheckBox chkRememberme;
    private Button btnLogin;
    private boolean chkflag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RepomsAPP.setDepartment("D000001");
        initView();

    }

    public void initView(){
        iconFont = Typeface.createFromAsset(getAssets(), "iconfonts/iconfont.ttf");
        //find view
        //TextView lblBtnConfig = findViewById(R.id.lblBtnConfig);
        btnLogin = findViewById(R.id.button2);
        txtUserName = findViewById(R.id.txtLoginUserName);
        txtUserPassword = findViewById(R.id.txtLoginUserPassword);
        chkAutoLogin = findViewById(R.id.chkLoginAutoLogin);
        chkRememberme = findViewById(R.id.chkLoginRememberme);
        //listener
        btnLogin.setOnClickListener(this);
        chkAutoLogin.setOnClickListener(this);
        chkRememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(chkflag){
                    chkflag = false;
                    return;
                }
                // TODO Auto-generated method stub
                if(isChecked){

                }else{
                    if(chkAutoLogin.isChecked()){
                        chkflag = true;
                        chkAutoLogin.setChecked(false);
                    }
                }
            }
        });
        chkAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(chkflag){
                    chkflag = false;
                    return;
                }
                // TODO Auto-generated method stub
                if(isChecked){
                    if(!chkRememberme.isChecked()){
                        chkflag = true;
                        chkRememberme.setChecked(true);
                    }
                }else{
                    if(chkRememberme.isChecked()){
                        chkflag = true;
                        chkRememberme.setChecked(false);
                    }
                }
            }
        });
        getPresenter().checkUserDb();
        getPresenter().readLastLogin();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button2: //登录
                LoginBean data = new LoginBean();
                data.setUsrname(txtUserName.getText().toString());
                data.setPasswd(txtUserPassword.getText().toString());
                data.setRememberme(chkRememberme.isChecked());
                data.setAutologin(chkAutoLogin.isChecked());
                getPresenter().login(data);
                break;
        }
    }
    @Override
    public LoginContact.LoginPtr onBindPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void loginSuccess(LoginBean login) {
        Toast.makeText(this,"登录成功",Toast.LENGTH_LONG).show();
        Intent starterMain = new Intent(this, MainActivity.class);
        starterMain.putExtra("user", login.getUsrname());
        startActivity(starterMain);
        finish();
    }

    @Override
    public void loginFailure(BaseResultBean result) {
        Toast.makeText(this,result.getErrMsg(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadLastLogin(LoginBean data) {
        //根据上次登录信息决定是自定登录还是加载用户名
        if(data.isAutologin()){
            /*
            * 根据保存的用户名和密码进行自动登录
            * */
            Log.i("自动登录", "开始自动登录");
            getPresenter().login(data);
        }
        else if(data.isRememberme()){
            /*
            * 加载用户名到输入框
            * */
            Log.i("记住用户名", "加载用户名");
            chkRememberme.setChecked(true);
            txtUserName.setText(data.getUsrname());
        }
    }
}

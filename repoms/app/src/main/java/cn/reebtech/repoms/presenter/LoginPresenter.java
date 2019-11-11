package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.LoginContact;
import cn.reebtech.repoms.model.LoginModel;

public class LoginPresenter extends BasePresenter<LoginContact.LoginUI, LoginBean, BaseResultBean> implements LoginContact.LoginPtr {
    private LoginContact.LoginMdl mLoginMdl;
    public static final int OP_READ_LASTLOGIN = 1;
    public static final int OP_LOGIN = 2;
    public static final int OP_SAVE_LASTLOGIN = 3;
    public static final int OP_CHECK_USERDB = 4;
    public LoginPresenter(@NonNull LoginContact.LoginUI view) {
        super(view);
        // 实例化 Model 层
        mLoginMdl=new LoginModel();
        initView();
    }
    /*
    * 初始化页面
    * */
    private void initView(){
        //读取上次登录信息

    }

    public void checkUserDb(){
        mLoginMdl.checkUserDb(this);
    }

    @Override
    public void login(LoginBean data) {
        //显示进度条
        getView().showLoading();
        //验证用户名密码是否有效
        if(data.getPasswd().equals("") || data.getUsrname().equals("")){
            getView().loginFailure(new BaseResultBean(2, getView().getSelfActivity().getString(R.string.str_tip_login_failed_up_invalid_null)));
        }
        else{
            mLoginMdl.login(data,this);
        }
    }

    @Override
    public void readLastLogin() {
        mLoginMdl.readLastLogin(this);
    }

    @Override
    public void cancel(Object tag) {

    }

    @Override
    public void cancelAll() {

    }

    @Override
    public void onSuccess(Integer source, LoginBean data, BaseResultBean result) {
        switch(source.intValue()){
            case OP_READ_LASTLOGIN:
                Log.i("onSuccess", "readLastLogin:" + data.getUsrname());
                getView().loadLastLogin(data);
                break;
            case OP_LOGIN:
                Log.i("onSuccess", "login");
                //根据用户选择的复选框执行相应的操作
                if(data.isRememberme() || data.isAutologin()){
                    mLoginMdl.saveLastLogin(data, this);
                }
                else{
                    getView().loginSuccess(data);
                }
                break;
            case OP_SAVE_LASTLOGIN:
                Log.i("onSuccess", "保存登录信息成功");
                getView().loginSuccess(data);
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        switch(source.intValue()){
            case OP_LOGIN:
                Log.i("onFailure", result.getErrMsg());
                getView().loginFailure(result);
                break;
            case OP_SAVE_LASTLOGIN:
                Log.i("onFailuer", "保存登录信息失败");
                break;
        }
    }
}

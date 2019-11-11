package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.MainContact;
import cn.reebtech.repoms.model.MainModel;

public class MainPresenter extends BasePresenter<MainContact.MainUI, LoginBean, BaseResultBean> implements MainContact.MainPtr{
    private MainContact.MainMdl mMainMdl;
    public static final int OP_LOGOUT = 1;
    public MainPresenter(@NonNull MainContact.MainUI view) {
        super(view);
        // 实例化 Model 层
        mMainMdl = new MainModel();
        //initView();
    }
    @Override
    public void logout() {
        mMainMdl.logout(this);
    }

    @Override
    public void onSuccess(Integer source, LoginBean data, BaseResultBean result) {
        switch(source.intValue()){
            case OP_LOGOUT:
                getView().logoutSuccess();
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {

    }

    @Override
    public void tmpInitData(int type){
        mMainMdl.tmpInitData(type);
    }
}

package cn.reebtech.repoms.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.view.IBaseView;

public abstract class BaseActivity<P extends IBasePresenter> extends BaseXActivity<P> implements IBaseView {
    // 加载进度框
    private ProgressDialog mProgressDialog;

    @Override
    public void showLoading(){

    }

    @Override
    public void hideLoading(){

    }

    @Override
    public void showToast(String msg){

    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}

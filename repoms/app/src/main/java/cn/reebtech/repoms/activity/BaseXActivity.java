package cn.reebtech.repoms.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import cn.reebtech.repoms.presenter.IBaseXPresenter;
import cn.reebtech.repoms.view.IBaseXView;

/*
* 基本的MVP架构的Activity的基类
* */
public abstract class BaseXActivity<P extends IBaseXPresenter> extends AppCompatActivity implements IBaseXView {
    private P mPresenter; //创建Presenter的引用
    /**
     * 创建 Presenter
     *
     * @return
     */
    public abstract P onBindPresenter();
    /**
     * 获取 Presenter 对象，在需要获取时才创建`Presenter`，起到懒加载作用
     */
    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = onBindPresenter();
        }
        return mPresenter;
    }
    @Override
    public Activity getSelfActivity() {
        return this;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束时，将 presenter 与 view 之间的联系断开，防止出现内存泄露
         */
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}

package cn.reebtech.repoms.base;

import android.os.Bundle;
import android.util.Log;

import cn.reebtech.repoms.presenter.BasePresenter;


/**
 * Created by on 2017/6/10 14:34 *
 * 作用: 公共的Mvp Activity 继承与BaseActivity
 *
 *  getLayoutId()   -> initView()  -> createPresenter() ->initData()
 *
 *   生命周期  onDestory-> 中包含  presenter 的 detachView 和 presenter的置空
 *
 *
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity {

   protected P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "oncreate BaseMvpActivity");
        mPresenter = createPresenter();
        initData();
    }

    protected abstract void initData();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    /**
     * 创建 Presenter
     *
     * @return
     */
    public abstract P createPresenter();
}

package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public abstract class BasePresenter<V extends IBaseView, M, N extends BaseResultBean> extends
        BaseXPresenter<V> implements IBasePresenter, IConDbListener<Integer, M, N> {
    public BasePresenter(@NonNull V view) {
        super(view);
    }
    @Override
    public void cancel(@NonNull Object tag) {

    }

    @Override
    public void cancelAll() {

    }
}

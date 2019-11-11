package cn.reebtech.repoms.presenter;

import cn.reebtech.repoms.bean.BaseResultBean;

public interface IDbOpPresenter<T, M extends BaseResultBean> {
    void save(T data);
    void edit(T data);
    void delete(String id);
    void onSuccess();
    void onFailure(M error);
}

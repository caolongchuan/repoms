package cn.reebtech.repoms.model;

import cn.reebtech.repoms.util.IConDbListener;

public interface IBaseDBModel<T,K,C extends IConDbListener> extends BaseModel {
    void insert(T record, C callback);
    void update(T record, K key, C callback);
    void delete(K key, C callback);
    void select(T record, C callback);
}

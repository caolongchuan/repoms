package cn.reebtech.repoms.model;

import java.util.List;

import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public abstract class BaseDBModel<T, K, C extends IConDbListener> implements IBaseDBModel<T,K,C> {
    public void insert(T record, C callback){

    }
    public void update(T record, K key, C callback){

    }
    public void delete(K key, C callback){

    }
    public void select(T record, C callback){

    }
    public void list(List<T> records, C callback){

    }
}

package cn.reebtech.repoms.model;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.model.entity.LastLogin;
import cn.reebtech.repoms.util.IConDbListener;

public class ConfigModelI implements IBaseDBModel<LastLogin, Long, IConDbListener>, IConDbListener<Integer, LastLogin, BaseResultBean> {

    @Override
    public void insert(LastLogin record, IConDbListener callback) {

    }

    @Override
    public void update(LastLogin record, Long key, IConDbListener callback) {

    }

    @Override
    public void delete(Long key, IConDbListener callback) {

    }

    @Override
    public void select(LastLogin record, IConDbListener callback) {

    }
    public static LastLogin getLastLogin(){
        LastLogin result = null;

        return result;
    }

    @Override
    public void onSuccess(Integer source, LastLogin data, BaseResultBean result) {

    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {

    }
}

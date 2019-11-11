package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.ServerBean;
import cn.reebtech.repoms.contact.ServerSetContact;
import cn.reebtech.repoms.model.ServerSetModel;

public class ServerSetPresenter extends BasePresenter<ServerSetContact.ServerSetUI, Object, BaseResultBean> implements ServerSetContact.ServerSetPtr {
    private ServerSetContact.ServerSetMdl mServerSetMdl;
    public static final int TYPE_OP_TEST_SRVSET = 1;
    public static final int TYPE_OP_SAVE_SRVSET = 2;
    public static final int TYPE_LOAD_CONFIG = 3;

    public ServerSetPresenter(@NonNull ServerSetContact.ServerSetUI view) {
        super(view);
        // 实例化 Model 层
        mServerSetMdl = new ServerSetModel(getView());
    }

    @Override
    public void testServerSet(ServerBean server) {
        getView().setTestStatus(true);
        mServerSetMdl.testServerSet(server, this);
    }

    @Override
    public void saveServerSet(ServerBean server) {
        mServerSetMdl.saveServerSet(server, this);
    }

    @Override
    public void loadCurrentConfig() {
        mServerSetMdl.loadCurrentConfig(this);
    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_OP_TEST_SRVSET:
                getView().showTestResult(result);
                break;
            case TYPE_OP_SAVE_SRVSET:
                getView().showSaveResult(result);
                break;
            case TYPE_LOAD_CONFIG:
                getView().showCurrentConfig((ServerBean) data);
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_OP_TEST_SRVSET:
                getView().showTestResult(result);
                break;
            case TYPE_OP_SAVE_SRVSET:
                getView().showSaveResult(result);
                break;
        }
    }
}

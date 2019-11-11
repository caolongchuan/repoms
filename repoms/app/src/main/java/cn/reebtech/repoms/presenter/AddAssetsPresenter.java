package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.contact.AddAssetContact;
import cn.reebtech.repoms.contact.AddAssetsContact;
import cn.reebtech.repoms.model.AddAssetModel;
import cn.reebtech.repoms.model.AddAssetsModel;

public class AddAssetsPresenter extends BasePresenter<AddAssetsContact.AddAssetsUI, Object, BaseResultBean> implements AddAssetsContact.AddAssetsPtr {
    private AddAssetsContact.AddAssetsMdl mAddAssetsMdl;
    public static final int TYPE_INIT_DATA_ALL = 1;
    public static final int TYPE_INIT_DATA_CLSFST = 2;
    public static final int TYPE_INIT_DATA_CLSSCD = 3;
    public static final int TYPE_LOAD_BINDED_RFIDS = 4;
    public static final int TYPE_LOAD_SUG_LOCATION = 5 ;
    public static final int TYPE_CHECK_RFID_VALID = 6;
    public static final int ERROR_CODE_LOAD_CLSFST_FAILED = 101;
    public static final int ERROR_CODE_LOAD_CLSSCD_FAILED = 102;
    public static final int ERROR_CODE_LOAD_BINDED_RFIDS_FAILED = 103;
    public AddAssetsPresenter(@NonNull AddAssetsContact.AddAssetsUI view) {
        super(view);
        // 实例化 Model 层
        mAddAssetsMdl = new AddAssetsModel();
        //initView();
    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_INIT_DATA_CLSFST:
                getView().fillSpinner(TYPE_INIT_DATA_CLSFST, (List<Map<String, String>>)data);
                break;
            case TYPE_INIT_DATA_CLSSCD:
                getView().fillSpinner(TYPE_INIT_DATA_CLSSCD, (List<Map<String, String>>)data);
                break;
            case TYPE_LOAD_BINDED_RFIDS:
                getView().setBindedRFIDs((List<String>) data);
                break;
            case TYPE_LOAD_SUG_LOCATION:
                getView().updateSugLocation((String) data);
                break;
            case TYPE_CHECK_RFID_VALID:
                getView().setRfidValidResult(((Map<String, Object>) data));
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        getView().showToast(result.getErrCode() + ":" + result.getErrMsg());
    }

    @Override
    public void initData(int type, String parent, String warehouse) {
        switch(type){
            case TYPE_INIT_DATA_ALL:
                mAddAssetsMdl.loadAssetClsFst(warehouse, this);
                break;
            case TYPE_INIT_DATA_CLSFST:
                mAddAssetsMdl.loadAssetClsFst(warehouse, this);
                break;
            case TYPE_INIT_DATA_CLSSCD:
                mAddAssetsMdl.loadAssetClsScd(parent, this);
                break;
        }
    }

    @Override
    public void loadBindedRFIDs() {
        mAddAssetsMdl.loadBindedRFIDs(this);
    }

    @Override
    public void loadSugLocation(String clssscd) {
        mAddAssetsMdl.loadSugLocation(clssscd, this);
    }

    @Override
    public void checkRfidValid(String rfid) {
        mAddAssetsMdl.checkRfidValid(rfid, this);
    }
}

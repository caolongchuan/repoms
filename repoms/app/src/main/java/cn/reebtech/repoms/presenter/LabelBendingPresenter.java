package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LabelBendingBean;
import cn.reebtech.repoms.contact.LabelBendingContact;
import cn.reebtech.repoms.model.LabelBendingModel;

public class LabelBendingPresenter extends BasePresenter<LabelBendingContact.LabelBendingUI, Object, BaseResultBean>
        implements LabelBendingContact.LabelBendingPtr {
    private LabelBendingContact.LabelBendingMdl mLadelBendingMdl;

    public static final int TYPE_INIT_DATA_ALL = 1;
    public static final int TYPE_INIT_DATA_CLSFST = 2;
    public static final int TYPE_INIT_DATA_CLSSCD = 3;
    public static final int TYPE_LOGIN_SERVER = 5;
    public static final int ERROR_CODE_LOAD_CLSFST_FAILED = 101;
    public static final int TYPE_INIT_DATA_BGS = 7;
    public static final int ERROR_CODE_LOAD_BGS_FAILED = 102;


    public LabelBendingPresenter(@NonNull LabelBendingContact.LabelBendingUI view) {
        super(view);
        mLadelBendingMdl = new LabelBendingModel(view);
    }

    @Override
    public void loadSugLocation(String clssscd) {
        mLadelBendingMdl.loadSugLocation(clssscd, this);
    }

    @Override
    public void initData(int type, String parent) {
        switch (type) {
            case TYPE_INIT_DATA_BGS:
                break;
            case TYPE_INIT_DATA_ALL:
//                mLadelBendingMdl.loadAssetClsFst(this);
                mLadelBendingMdl.loadBGS(this);
                break;
            case TYPE_INIT_DATA_CLSFST:
                mLadelBendingMdl.loadAssetClsFst(this,parent);
                break;
            case TYPE_INIT_DATA_CLSSCD:
                mLadelBendingMdl.loadAssetClsScd(parent, this);
                break;
        }
    }

    @Override
    public void saveOrder(LabelBendingBean data) {
        mLadelBendingMdl.saveOrder(data, this);
    }

    @Override
    public void loadAsset(String rfid) {

    }

    @Override
    public void toLocalList() {

    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch (source.intValue()) {
            case TYPE_INIT_DATA_BGS:
                getView().fillSpinner(TYPE_INIT_DATA_BGS, (List<Map<String, String>>) data);
                break;
            case TYPE_INIT_DATA_CLSFST:
                getView().fillSpinner(TYPE_INIT_DATA_CLSFST, (List<Map<String, String>>) data);
                break;
            case TYPE_INIT_DATA_CLSSCD:
                getView().fillSpinner(TYPE_INIT_DATA_CLSSCD, (List<Map<String, String>>) data);
                break;
            case TYPE_LOGIN_SERVER:
                getView().showToast("登录服务器成功，开始下载未绑定标签物资");
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        getView().showToast(result.getErrCode() + ":" + result.getErrMsg());
    }


}

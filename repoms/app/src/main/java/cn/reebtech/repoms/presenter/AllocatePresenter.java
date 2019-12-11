package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.contact.AllocateContact;
import cn.reebtech.repoms.model.AllocateModel;

public class AllocatePresenter extends BasePresenter<AllocateContact.AllocateUI, Object, BaseResultBean>
        implements AllocateContact.AllocatePtr {
    private AllocateContact.AllocateMdl mAllocateMdl;

    public static final int TYPE_INIT_DATA_ALL = 0;
    public static final int TYPE_INIT_DATA_BGS = 1;
    public static final int TYPE_INIT_DATA_CLSSCD = 2;
    public static final int TYPE_INIT_DATA_CLSFST = 3;
    public static final int ERROR_CODE_LOAD_BGS_FAILED = 10;
    public static final int ERROR_CODE_LOAD_CLSSCD_FAILED = 11;

    public AllocatePresenter(@NonNull AllocateContact.AllocateUI view) {
        super(view);
        mAllocateMdl = new AllocateModel(view);
    }

    @Override
    public void initData(int type, String parent) {
        switch (type) {
            case TYPE_INIT_DATA_ALL:
                mAllocateMdl.loadBGS(this);
                break;
//            case TYPE_INIT_DATA_CLSSCD:
//                mAllocateMdl.loadAssetClsScd(parent, this);
//                break;
//            case TYPE_INIT_DATA_CLSFST:
//                mAllocateMdl.loadAssetClsFst(this,parent);
//                break;
        }

    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch (source.intValue()) {
            case TYPE_INIT_DATA_BGS:
                getView().fillSpinner(TYPE_INIT_DATA_BGS, (List<Map<String, String>>) data);
                break;
            case TYPE_INIT_DATA_CLSSCD:
                getView().fillSpinner(TYPE_INIT_DATA_CLSSCD, (List<Map<String, String>>) data);
                break;
            case TYPE_INIT_DATA_CLSFST:
                getView().fillSpinner(TYPE_INIT_DATA_CLSFST, (List<Map<String, String>>) data);
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {

    }
}

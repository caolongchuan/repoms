package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.contact.ScanContact;
import cn.reebtech.repoms.model.ScanModel;

public class ScanPresenter extends BasePresenter<ScanContact.ScanUI, Object, BaseResultBean> implements ScanContact.ScanPtr {
    private ScanContact.ScanMdl mScanMdl;
    public static final int TYPE_LOAD_ASSET = 1;
    public ScanPresenter(@NonNull ScanContact.ScanUI view) {
        super(view);
        // 实例化 Model 层
        mScanMdl = new ScanModel();
    }
    @Override
    public void loadAsset(String rfid) {
        mScanMdl.loadAsset(rfid, this);
    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        getView().loadAssetInfo((AssetBean) data);
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        getView().showToast(result.getErrMsg());
    }
}

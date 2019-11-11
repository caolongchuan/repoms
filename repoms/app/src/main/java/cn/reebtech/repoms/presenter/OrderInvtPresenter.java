package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderInvtBean;
import cn.reebtech.repoms.bean.OrderRetBean;
import cn.reebtech.repoms.contact.OrderInvtContact;
import cn.reebtech.repoms.contact.OrderRetContact;
import cn.reebtech.repoms.model.OrderInvtModel;
import cn.reebtech.repoms.model.OrderRetModel;
import cn.reebtech.repoms.presenter.BasePresenter;

public class OrderInvtPresenter extends BasePresenter<OrderInvtContact.OrderInvtUI, Object, BaseResultBean> implements OrderInvtContact.OrderInvtPtr {
    private OrderInvtContact.OrderInvtMdl mOrderInvtMdl;
    public static final int TYPE_LOAD_DATA_WAREHOUSE = 1;
    public static final int TYPE_LOAD_DATA_FROMUSERS = 2;
    public static final int TYPE_LOAD_DATA_MGRS = 3;
    public static final int TYPE_SAVE_ORDER = 4;
    public static final int TYPE_LOAD_ASSET = 5;
    public OrderInvtPresenter(@NonNull OrderInvtContact.OrderInvtUI view) {
        super(view);
        // 实例化 Model 层
        mOrderInvtMdl = new OrderInvtModel();
    }
    @Override
    public void initData() {
        mOrderInvtMdl.loadWareHouse(this);
    }

    @Override
    public void saveOrder(OrderInvtBean data) {
        mOrderInvtMdl.saveOrder(data, this);
    }

    @Override
    public void loadAsset(String rfid) {
        mOrderInvtMdl.loadAsset(rfid, this);
    }

    @Override
    public void toLocalList() {

    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_LOAD_DATA_WAREHOUSE:
                getView().fillSpinner(TYPE_LOAD_DATA_WAREHOUSE, (List<Map<String, String>>)data);
                mOrderInvtMdl.loadMgrs(this);
                break;
            case TYPE_LOAD_DATA_MGRS:
                getView().fillSpinner(TYPE_LOAD_DATA_MGRS, (List<Map<String, String>>)data);
                break;
            case TYPE_SAVE_ORDER:
                getView().onSaveSuccess();
                break;
            case TYPE_LOAD_ASSET:
                getView().addScanedAsset((AssetBean) data);
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        getView().showToast(result.getErrMsg());
    }
}

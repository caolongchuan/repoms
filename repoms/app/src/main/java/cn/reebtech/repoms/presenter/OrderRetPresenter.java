package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderReqBean;
import cn.reebtech.repoms.bean.OrderRetBean;
import cn.reebtech.repoms.contact.OrderReqContact;
import cn.reebtech.repoms.contact.OrderRetContact;
import cn.reebtech.repoms.model.OrderReqModel;
import cn.reebtech.repoms.model.OrderRetModel;

public class OrderRetPresenter extends BasePresenter<OrderRetContact.OrderRetUI, Object, BaseResultBean> implements OrderRetContact.OrderRetPtr {
    private OrderRetContact.OrderRetMdl mOrderRetMdl;
    public static final int TYPE_LOAD_DATA_WAREHOUSE = 1;
    public static final int TYPE_LOAD_DATA_FROMUSERS = 2;
    public static final int TYPE_LOAD_DATA_MGRS = 3;
    public static final int TYPE_SAVE_ORDER = 4;
    public static final int TYPE_LOAD_ASSET = 5;
    public OrderRetPresenter(@NonNull OrderRetContact.OrderRetUI view) {
        super(view);
        // 实例化 Model 层
        mOrderRetMdl = new OrderRetModel();
    }
    @Override
    public void initData() {
        mOrderRetMdl.loadWareHouse(this);
    }

    @Override
    public void saveOrder(OrderRetBean data) {
        mOrderRetMdl.saveOrder(data, this);
    }

    @Override
    public void loadAsset(String rfid) {
        mOrderRetMdl.loadAsset(rfid, this);
    }

    @Override
    public void toLocalList() {

    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_LOAD_DATA_WAREHOUSE:
                getView().fillSpinner(TYPE_LOAD_DATA_WAREHOUSE, (List<Map<String, String>>)data);
                mOrderRetMdl.loadFromUsers(this);
                break;
            case TYPE_LOAD_DATA_FROMUSERS:
                getView().fillSpinner(TYPE_LOAD_DATA_FROMUSERS, (List<Map<String, String>>)data);
                mOrderRetMdl.loadMgrs(this);
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

package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderReqBean;
import cn.reebtech.repoms.contact.OrderReqContact;
import cn.reebtech.repoms.model.OrderReqModel;

public class OrderReqPresenter extends BasePresenter<OrderReqContact.OrderReqUI, Object, BaseResultBean> implements OrderReqContact.OrderReqPtr {
    private OrderReqContact.OrderReqMdl mOrderReqMdl;
    public static final int TYPE_LOAD_DATA_WAREHOUSE = 1;
    public static final int TYPE_LOAD_DATA_FROMUSERS = 2;
    public static final int TYPE_LOAD_DATA_MGRS = 3;
    public static final int TYPE_SAVE_ORDER = 4;
    public static final int TYPE_LOAD_ASSET = 5;
    public OrderReqPresenter(@NonNull OrderReqContact.OrderReqUI view) {
        super(view);
        // 实例化 Model 层
        mOrderReqMdl = new OrderReqModel();
    }
    @Override
    public void initData() {
        mOrderReqMdl.loadWareHouse(this);
    }

    @Override
    public void saveOrder(OrderReqBean data) {
        mOrderReqMdl.saveOrder(data, this);
    }

    @Override
    public void loadAsset(String rfid) {
        mOrderReqMdl.loadAsset(rfid, this);
    }

    @Override
    public void toLocalList() {

    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_LOAD_DATA_WAREHOUSE:
                getView().fillSpinner(TYPE_LOAD_DATA_WAREHOUSE, (List<Map<String, String>>)data);
                mOrderReqMdl.loadFromUsers(this);
                break;
            case TYPE_LOAD_DATA_FROMUSERS:
                getView().fillSpinner(TYPE_LOAD_DATA_FROMUSERS, (List<Map<String, String>>)data);
                mOrderReqMdl.loadMgrs(this);
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

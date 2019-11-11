package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderOutBean;
import cn.reebtech.repoms.contact.OrderOutContact;
import cn.reebtech.repoms.model.OrderOutModel;

public class OrderOutPresenter extends BasePresenter<OrderOutContact.OrderOutUI, Object, BaseResultBean> implements OrderOutContact.OrderOutPtr {
    private OrderOutContact.OrderOutMdl mOrderOutMdl;
    public static final int TYPE_LOAD_DATA_WAREHOUSE = 1;
    public static final int TYPE_LOAD_DATA_FROMUSERS = 2;
    public static final int TYPE_LOAD_DATA_MGRS = 3;
    public static final int TYPE_SAVE_ORDER = 4;
    public static final int TYPE_LOAD_ASSET = 5;

    public OrderOutPresenter(@NonNull OrderOutContact.OrderOutUI view) {
        super(view);
        // 实例化 Model 层
        mOrderOutMdl = new OrderOutModel();
    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_LOAD_DATA_WAREHOUSE:
                getView().fillSpinner(TYPE_LOAD_DATA_WAREHOUSE, (List<Map<String, String>>)data);
                mOrderOutMdl.loadFromUsers(this);
                break;
            case TYPE_LOAD_DATA_FROMUSERS:
                getView().fillSpinner(TYPE_LOAD_DATA_FROMUSERS, (List<Map<String, String>>)data);
                mOrderOutMdl.loadMgrs(this);
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

    @Override
    public void initData() {
        mOrderOutMdl.loadWareHouse(this);
    }

    @Override
    public void saveOrder(OrderOutBean data) {
        mOrderOutMdl.saveOrder(data, this);
    }

    @Override
    public void loadAsset(String rfid) {
        mOrderOutMdl.loadAsset(rfid, this);
    }

    @Override
    public void toLocalList() {

    }
}

package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderInBean;
import cn.reebtech.repoms.contact.OrderInContact;
import cn.reebtech.repoms.model.OrderInModel;

public class OrderInPresenter extends BasePresenter<OrderInContact.OrderInUI, Object, BaseResultBean> implements OrderInContact.OrderInPtr, View.OnClickListener {
    private OrderInContact.OrderInMdl mOrderInMdl;
    public static final int TYPE_LOAD_DATA_WAREHOUSE = 1;
    public static final int TYPE_LOAD_DATA_FROMUSERS = 2;
    public static final int TYPE_LOAD_DATA_MGRS = 3;
    public static final int TYPE_SAVE_ORDER = 4;

    public OrderInPresenter(@NonNull OrderInContact.OrderInUI view) {
        super(view);
        // 实例化 Model 层
        mOrderInMdl = new OrderInModel();
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void initData() {
        mOrderInMdl.loadWareHouse(this);
    }

    @Override
    public void saveOrder(OrderInBean data) {
        mOrderInMdl.saveOrder(data, this);
    }

    @Override
    public void toLocalList() {

    }

    @Override
    public void toAssetAdd() {

    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case TYPE_LOAD_DATA_WAREHOUSE:
                getView().fillSpinner(TYPE_LOAD_DATA_WAREHOUSE, (List<Map<String, String>>)data);
                mOrderInMdl.loadFromUsers(this);
                break;
            case TYPE_LOAD_DATA_FROMUSERS:
                getView().fillSpinner(TYPE_LOAD_DATA_FROMUSERS, (List<Map<String, String>>)data);
                mOrderInMdl.loadMgrs(this);
                break;
            case TYPE_LOAD_DATA_MGRS:
                getView().fillSpinner(TYPE_LOAD_DATA_MGRS, (List<Map<String, String>>)data);
                break;
            case TYPE_SAVE_ORDER:
                getView().onSaveSuccess();
                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        Log.i("error:", result.getErrMsg());
        getView().showToast(result.getErrMsg());
    }
}

package cn.reebtech.repoms.presenter;

import android.support.annotation.NonNull;

import java.text.BreakIterator;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderOutBean;
import cn.reebtech.repoms.contact.OrderListContact;
import cn.reebtech.repoms.contact.OrderOutContact;
import cn.reebtech.repoms.model.OrderListModel;
import cn.reebtech.repoms.model.OrderOutModel;

public class OrderListPresenter extends BasePresenter<OrderListContact.OrderListUI, Object, BaseResultBean> implements OrderListContact.OrderListPtr {
    private OrderListContact.OrderListMdl mOrderListMdl;
    public static final int TYPE_ORDER_IN = 1;
    public static final int TYPE_ORDER_OUT = 2;
    public static final int TYPE_ORDER_REQ = 3;
    public static final int TYPE_ORDER_RET = 4;
    public static final int TYPE_DELETE_ITEM = 5;
    public static final int TYPE_ORDER_INV = 6;

    public OrderListPresenter(@NonNull OrderListContact.OrderListUI view) {
        super(view);
        // 实例化 Model 层
        mOrderListMdl = new OrderListModel();
    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        if(source.intValue() == TYPE_DELETE_ITEM){
            getView().onRemoveItemSuccess();
        }
        else{
            getView().fillList((List<Map<String, Object>>)data);
        }

    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        getView().showToast(result.getErrMsg());
    }

    @Override
    public void loadData(int type) {
        mOrderListMdl.loadData(type, this);
    }

    @Override
    public void deleteData(int type, String id) {
        mOrderListMdl.deleteData(type, id, this);
    }
}

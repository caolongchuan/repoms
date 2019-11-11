package cn.reebtech.repoms.contact;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.OrderOutBean;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public class OrderListContact {
    /**
     * view 层接口
     */
    public interface OrderListUI extends IBaseView {
        void fillList(List<Map<String, Object>> data);
        void onRemoveItemSuccess();
    }
    /**
     * presenter 层接口
     */
    public interface OrderListPtr extends IBasePresenter {
        void loadData(int type);
        void deleteData(int type, String id);
    }
    /**
     * model 层接口
     */
    public interface OrderListMdl {
        void loadData(int type, IConDbListener callback);
        void deleteData(int type, String id, IConDbListener callback);
    }
}

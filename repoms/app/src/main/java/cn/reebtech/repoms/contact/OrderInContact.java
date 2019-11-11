package cn.reebtech.repoms.contact;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderInBean;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public class OrderInContact {
    /**
     * view 层接口
     */
    public interface OrderInUI extends IBaseView {
        void onSaveSuccess();
        void onSaveFailure(BaseResultBean result);
        void fillSpinner(int type, List<Map<String, String>> data);
    }
    /**
     * presenter 层接口
     */
    public interface OrderInPtr extends IBasePresenter {
        void initData();
        void saveOrder(OrderInBean data);
        void toLocalList();
        void toAssetAdd();
    }
    /**
     * model 层接口
     */
    public interface OrderInMdl {
        void saveOrder(OrderInBean data, IConDbListener callback);
        void loadWareHouse(IConDbListener callback);
        void loadFromUsers(IConDbListener callback);
        void loadMgrs(IConDbListener callback);
    }
}

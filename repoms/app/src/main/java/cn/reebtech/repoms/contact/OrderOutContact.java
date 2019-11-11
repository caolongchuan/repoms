package cn.reebtech.repoms.contact;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderInBean;
import cn.reebtech.repoms.bean.OrderOutBean;
import cn.reebtech.repoms.model.OrderOutModel;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public final class OrderOutContact {
    /**
     * view 层接口
     */
    public interface OrderOutUI extends IBaseView {
        void onSaveSuccess();
        void fillSpinner(int type, List<Map<String, String>> data);
        void addScanedAsset(AssetBean data);
    }
    /**
     * presenter 层接口
     */
    public interface OrderOutPtr extends IBasePresenter {
        void initData();
        void saveOrder(OrderOutBean data);
        void loadAsset(String rfid);
        void toLocalList();
    }
    /**
     * model 层接口
     */
    public interface OrderOutMdl {
        void saveOrder(OrderOutBean data, IConDbListener callback);
        void loadWareHouse(IConDbListener callback);
        void loadFromUsers(IConDbListener callback);
        void loadMgrs(IConDbListener callback);
        void loadAsset(String rfid, IConDbListener callback);
    }
}

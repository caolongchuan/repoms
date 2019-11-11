package cn.reebtech.repoms.contact;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.OrderRetBean;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public final class OrderRetContact {
    /**
     * view 层接口
     */
    public interface OrderRetUI extends IBaseView {
        void onSaveSuccess();
        void fillSpinner(int type, List<Map<String, String>> data);
        void addScanedAsset(AssetBean data);
    }
    /**
     * presenter 层接口
     */
    public interface OrderRetPtr extends IBasePresenter {
        void initData();
        void saveOrder(OrderRetBean data);
        void loadAsset(String rfid);
        void toLocalList();
    }
    /**
     * model 层接口
     */
    public interface OrderRetMdl {
        void saveOrder(OrderRetBean data, IConDbListener callback);
        void loadWareHouse(IConDbListener callback);
        void loadFromUsers(IConDbListener callback);
        void loadMgrs(IConDbListener callback);
        void loadAsset(String rfid, IConDbListener callback);
    }
}

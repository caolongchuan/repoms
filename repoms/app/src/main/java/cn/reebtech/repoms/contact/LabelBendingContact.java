package cn.reebtech.repoms.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.LabelBendingBean;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public final class LabelBendingContact {
    /**
     * view 层接口
     */
    public interface LabelBendingUI extends IBaseView {
        void onSaveSuccess();
        void fillSpinner(int type, List<Map<String, String>> data);
        void addScanedAsset(AssetBean data);
    }
    /**
     * presenter 层接口
     */
    public interface LabelBendingPtr extends IBasePresenter {
        void initData(int type, String parent);
        void saveOrder(LabelBendingBean data);

        void loadSugLocation(String clssscd);
        void loadAsset(String rfid);
        void toLocalList();
    }
    /**
     * model 层接口
     */
    public interface LabelBendingMdl {
        void loadBGS(IConDbListener callback);
        void loadAssetClsFst(IConDbListener callback,String bgs_key);
        void loadAssetClsScd(String parent, IConDbListener callback);
        void loadSugLocation(String clssscd, IConDbListener callback);

        void saveOrder(LabelBendingBean data, IConDbListener callback);
        void loadWareHouse(IConDbListener callback);
        void loadFromUsers(IConDbListener callback);
        void loadMgrs(IConDbListener callback);
        void loadAsset(String rfid, IConDbListener callback);
    }

}

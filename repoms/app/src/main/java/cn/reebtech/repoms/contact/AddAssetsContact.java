package cn.reebtech.repoms.contact;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public class AddAssetsContact {
    /**
     * view 层接口
     */
    public interface AddAssetsUI extends IBaseView {
        void saveAssets();
        void fillSpinner(int type, List<Map<String, String>> data);
        void setBindedRFIDs(List<String> rfids);
        void updateSugLocation(String location);
        void setRfidValidResult(Map<String, Object> result);
    }
    /**
     * presenter 层接口
     */
    public interface AddAssetsPtr extends IBasePresenter {
        void initData(int type, String parent, String warehouse);
        void loadBindedRFIDs();
        void loadSugLocation(String clssscd);
        void checkRfidValid(String rfid);
    }
    /**
     * model 层接口
     */
    public interface AddAssetsMdl {
        void loadAssetClsFst(String warehouse, IConDbListener callback);
        void loadAssetClsScd(String parent, IConDbListener callback);
        void loadBindedRFIDs(IConDbListener callback);
        void loadSugLocation(String clssscd, IConDbListener callback);
        void checkRfidValid(String rfid, IConDbListener callback);
    }
}

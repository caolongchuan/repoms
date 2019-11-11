package cn.reebtech.repoms.contact;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public final class ScanContact {
    /**
     * view 层接口
     */
    public interface ScanUI extends IBaseView {
        void loadAssetInfo(AssetBean asset);
    }
    /**
     * presenter 层接口
     */
    public interface ScanPtr extends IBasePresenter {
        void loadAsset(String rfid);
    }
    /**
     * model 层接口
     */
    public interface ScanMdl {
        void loadAsset(String rfid, IConDbListener callback);
    }
}

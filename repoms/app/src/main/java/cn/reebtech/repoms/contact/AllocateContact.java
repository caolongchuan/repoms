package cn.reebtech.repoms.contact;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public class AllocateContact {
    /**
     * view 层接口
     */
    public interface AllocateUI extends IBaseView {
        void fillSpinner(int type, List<Map<String, String>> data);
    }
    /**
     * presenter 层接口
     */
    public interface AllocatePtr extends IBasePresenter {
        void initData(int typeInitDataAll, String s);
    }
    /**
     * model 层接口
     */
    public interface AllocateMdl {
        void loadBGS(IConDbListener callback);

        void loadAssetClsScd(String parent, IConDbListener callback);

        void loadAssetClsFst(IConDbListener callback,String bgs_key);
    }

}

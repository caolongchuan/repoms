package cn.reebtech.repoms.contact;

import cn.reebtech.repoms.bean.BaseResult;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.ServerBean;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public class ServerSetContact {
    /**
     * view 层接口
     */
    public interface ServerSetUI extends IBaseView {
        void showLoading();
        void showTestResult(BaseResultBean result);
        void setTestStatus(boolean status);
        void showSaveResult(BaseResultBean result);
        void showCurrentConfig(ServerBean server);
    }
    /**
     * presenter 层接口
     */
    public interface ServerSetPtr extends IBasePresenter {
        void testServerSet(ServerBean server);
        void saveServerSet(ServerBean server);
        void loadCurrentConfig();
    }
    /**
     * model 层接口
     */
    public interface ServerSetMdl {
        void testServerSet(ServerBean server, IConDbListener callback);
        void saveServerSet(ServerBean server, IConDbListener callback);
        void loadCurrentConfig(IConDbListener callback);
    }
}

package cn.reebtech.repoms.contact;

import android.content.Context;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.presenter.BasePresenter;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public final class DataSyncContact {
    /*
    * 定义View接口
    * */
    public interface DataSyncUI extends IBaseView{
        void onDownloadComplete(BaseResultBean result);
        void onUploadComplete(BaseResultBean result);
        void fillRecycleView(int type, List<Map<String, Object>> data);
    }

    /**
     * presenter 层接口
     */
    public interface DataSyncPtr extends IBasePresenter {
        void downloadItems(List<Integer> items);
        void uploadItems(List<Integer> items);
        void initData();
    }

    /**
     * model 层接口
     */
    public interface DataSyncMdl {
        void loadDownloadItems(Context context, IConDbListener callback);
        void loadUploadItems(Context context, IConDbListener callback);
        void downloadItems(List<Integer> items, IConDbListener callback);
        void uploadItems(List<Integer> items, IConDbListener callback);
        void startDataSync(int type, IConDbListener callback);
        boolean checkDlValid();
        void syncNext(int type);
    }
}

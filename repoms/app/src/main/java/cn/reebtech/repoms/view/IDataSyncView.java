package cn.reebtech.repoms.view;

import cn.reebtech.repoms.bean.BaseResultBean;

public interface IDataSyncView extends IBaseView {
    void onDownloadResult(BaseResultBean result);
    void onUploadResult(BaseResultBean result);
}

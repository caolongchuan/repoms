package cn.reebtech.repoms.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.activity.DataSyncActivity;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.DataSyncContact;
import cn.reebtech.repoms.contact.MainContact;
import cn.reebtech.repoms.model.DataSyncModel;
import cn.reebtech.repoms.model.MainModel;

public class DataSyncPresenter extends BasePresenter<DataSyncContact.DataSyncUI, Object, BaseResultBean> implements DataSyncContact.DataSyncPtr{
    private DataSyncContact.DataSyncMdl mDataSyncMdl;
    public static final int OP_LOAD_DOWNLOAD_ITEMS = 1;
    public static final int OP_LOAD_UPLOAD_ITEMS = 2;
    public static final int TYPE_DOWNLOAD = 3;
    public static final int TYPE_UPLOAD = 4;
    public static final int TYPE_LOGIN_SERVER = 5;
    public static final int TYPE_CHECK_DOWNL_VALID = 6;
    public static final int TYPE_DOWN_ITEM_COMPLETE = 7;
    public static final int TYPE_DOWN_COMPLETE = 8;
    public static final int TYPE_UP_ITEM_COMPLETE = 9;
    public static final int TYPE_UP_COMPLETE = 10;
    public DataSyncPresenter(Context context, @NonNull DataSyncContact.DataSyncUI view) {
        super(view);
        // 实例化 Model 层
        mDataSyncMdl = new DataSyncModel(context,view);
        //initView();
    }

    @Override
    public void onSuccess(Integer source, Object data, BaseResultBean result) {
        switch(source.intValue()){
            case OP_LOAD_DOWNLOAD_ITEMS:
                getView().fillRecycleView(OP_LOAD_DOWNLOAD_ITEMS, (List<Map<String, Object>>) data);
                mDataSyncMdl.loadUploadItems(getView().getSelfActivity(), this);
                break;
            case OP_LOAD_UPLOAD_ITEMS:
                getView().fillRecycleView(OP_LOAD_UPLOAD_ITEMS, (List<Map<String, Object>>) data);
                break;
            case TYPE_LOGIN_SERVER:
                getView().showToast("登录服务器成功，开始数据同步");
                mDataSyncMdl.startDataSync((int) data, this);
                break;
            case TYPE_DOWN_ITEM_COMPLETE:
                mDataSyncMdl.syncNext(TYPE_DOWNLOAD);
                mDataSyncMdl.startDataSync((int) data, this);
                break;
            case TYPE_DOWN_COMPLETE:
                getView().showToast("下载数据完成");
                break;
            case TYPE_UP_ITEM_COMPLETE:
                mDataSyncMdl.syncNext(TYPE_UPLOAD);
                mDataSyncMdl.startDataSync((int) data, this);
                break;
            case TYPE_UP_COMPLETE:
                getView().showToast("上传数据完成");
                break;
            case TYPE_DOWNLOAD:

                break;
            case TYPE_UPLOAD:

                break;
        }
    }

    @Override
    public void onFailure(Integer source, BaseResultBean result) {
        getView().showToast(result.getErrMsg());
    }

    @Override
    public void downloadItems(List<Integer> items) {
        if(mDataSyncMdl.checkDlValid()){
            mDataSyncMdl.downloadItems(items, this);
        }
        else{
            getView().showToast(((DataSyncActivity)getView()).getString(R.string.str_err_msg_sync_downl_invalid));
        }
    }

    @Override
    public void uploadItems(List<Integer> items) {
        mDataSyncMdl.uploadItems(items, this);
    }

    @Override
    public void initData() {
        mDataSyncMdl.loadDownloadItems(getView().getSelfActivity(), this);
    }
}

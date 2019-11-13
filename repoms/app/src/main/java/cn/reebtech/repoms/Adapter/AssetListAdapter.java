package cn.reebtech.repoms.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.ViewHolder>{
    private List<Map<String, Object>> mDatas = null;
    private LayoutInflater mInflater = null;
    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    /*
    * 默认构造方法
    * */
    public AssetListAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = new ArrayList<Map<String, Object>>();
    }
    public AssetListAdapter(Context context, List<Map<String, Object>> datas) {
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(context);
    }

    // 创建新View，被LayoutManager所调用
    @Override
    public AssetListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rcy_item_asset, parent, false);
        ViewHolder vewHolder = new ViewHolder(view);
        return vewHolder;
    }

    /*
    * 将数据与界面进行绑定
    * */
    @Override
    public void onBindViewHolder(final AssetListAdapter.ViewHolder holder, final int position) {
        final Map<String, Object> item = mDatas.get(position);
//        holder.assetNum.setText("资产编码:" + item.get("num"));
        holder.assetNum.setText("资产编码:" + item.get("asset_code"));
        holder.assetName.setText(String.valueOf(item.get("name")));
        // 点击事件注册及分发
        if(null != mOnItemClickListener) {
            holder.assetTrush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.assetTrush, position);
                    Log.i("item_click:", String.valueOf(item.get("name")));
                }
            });
            /*
            holder.assetNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.titleTv, position);
                }
            });
            holder.assetName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.contenTv, position);
                }
            });
            */
        }
        // 长按事件注册及分发
        if(null != mOnItemLongClickListener) {
            /*
            holder.titleTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onLongClick(holder.titleTv, position);
                }
            });
            holder.contenTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onLongClick(holder.contenTv, position);
                }
            });
            */
        }
    }

    /*
    * 获取记录数量
    * */
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    // 设置点击事件
    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    // 在对应位置增加一个item
    public void addData(int position, Map<String, Object> item) {
        position = position <= 0 ? 0 : position;
        mDatas.add(position, item);
        try{
            notifyItemInserted(position);
            if(position != getItemCount()) {
                notifyItemRangeChanged(position, getItemCount());
            }
        }
        catch(Exception e){
            Log.i("Err", "update record failed:" + e.getMessage());
        }
    }

    // 删除对应item
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        if(position != getItemCount()) {
            notifyItemRangeChanged(position, getItemCount());
        }
        updateData();
    }

    public void removeAll(){
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void updateData(){
        for(int i = 0; i < mDatas.size(); i++){
            Map<String, Object> item = mDatas.get(i);
            item.put("num", (i+1));
        }
        notifyDataSetChanged();
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onClick(View parent, int position);
    }

    // 长按事件接口
    public interface OnItemLongClickListener {
        boolean onLongClick(View parent, int position);
    }

    // 设置长按事件
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        this.mOnItemLongClickListener = l;
    }

    // 自定义的ViewHolder，持有每个Item的的所有界面组件
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView assetNum = null;
        public TextView assetName = null;
        public ImageButton assetTrush = null;

        public ViewHolder(View itemView) {
            super(itemView);
            assetNum = (TextView) itemView.findViewById(R.id.txt_asset_num);
            assetName = (TextView) itemView.findViewById(R.id.txt_asset_name);
            assetTrush = (ImageButton) itemView.findViewById(R.id.imgbtn_asset_trush);
        }
    }
}

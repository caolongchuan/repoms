package cn.reebtech.repoms.util;

import android.content.Context;
import android.opengl.Visibility;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.presenter.OrderListPresenter;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{
    private List<Map<String, Object>> mDatas = null;
    private LayoutInflater mInflater = null;
    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    public static final int ORDER_TYPE_IN = 1;
    public static final int ORDER_TYPE_OUT = 2;
    public static final int ORDER_TYPE_REQ = 3;
    public static final int ORDER_TYPE_RET = 4;
    private int listType;
    /*
    * 默认构造方法
    * */
    public OrderListAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = new ArrayList<Map<String, Object>>();
    }

    public OrderListAdapter(Context context, List<Map<String, Object>> datas) {
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(context);
    }

    public OrderListAdapter(Context context, int listType){
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = new ArrayList<Map<String, Object>>();
        this.listType = listType;
    }

    // 创建新View，被LayoutManager所调用
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rcy_order_list_item, parent, false);
        ViewHolder vewHolder = new ViewHolder(view, this.listType);
        return vewHolder;
    }

    /*
    * 将数据与界面进行绑定
    * */
    @Override
    public void onBindViewHolder(final OrderListAdapter.ViewHolder holder, final int position) {
        final Map<String, Object> item = mDatas.get(position);
        holder.txtVueId.setText("" + item.get("id"));
        holder.txtVueWhouse.setText("" + item.get("whouse"));
        if(listType == OrderListPresenter.TYPE_ORDER_INV){
            holder.txtVueCont.setText("" + item.get("mgr"));
            holder.txtVueAcount.setText("" + item.get("acount"));
            holder.txtVueOdate.setText("" + item.get("odate"));
        }
        else{
            holder.txtVueCont.setText("" + item.get("cont"));
            holder.txtVueMgr.setText("" + item.get("mgr"));
            holder.txtVueAcount.setText("" + item.get("acount"));
            holder.txtVueOdate.setText("" + item.get("odate"));
        }
        // 点击事件注册及分发
        if(null != mOnItemClickListener) {
            holder.btnTrush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.btnTrush, position);
                    Log.i("item_click:", String.valueOf(item.get("name")));
                }
            });
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
    }

    public void removeAll(){
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<Map<String, Object>> data){
        this.mDatas = data;
        notifyDataSetChanged();
    }
    public Map<String, Object> getData(int position){
        if(position >= 0 && position < mDatas.size()){
            return mDatas.get(position);
        }
        else{
            return new HashMap<String, Object>();
        }
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
        public TextView txtVueId;
        public TextView txtVueWhouse;
        public TextView txtLblCont;
        public TextView txtVueCont;
        public TextView txtLblMgr;
        public TextView txtVueMgr;
        public TextView txtLblAcount;
        public TextView txtVueAcount;
        public TextView txtLblOdate;
        public TextView txtVueOdate;
        public ImageButton btnTrush = null;

        public ViewHolder(View itemView, int listType) {
            super(itemView);
            txtVueId = (TextView) itemView.findViewById(R.id.txt_order_item_vue_id);
            txtVueWhouse = (TextView) itemView.findViewById(R.id.txt_order_item_vue_whouse);
            txtLblCont = (TextView) itemView.findViewById(R.id.txt_order_item_lbl_cont);
            txtVueCont = (TextView) itemView.findViewById(R.id.txt_order_item_vue_cont);
            txtLblMgr = (TextView) itemView.findViewById(R.id.txt_order_item_lbl_mgr);
            txtVueMgr = (TextView) itemView.findViewById(R.id.txt_order_item_vue_mgr);
            txtLblAcount = (TextView) itemView.findViewById(R.id.txt_order_item_lbl_acount);
            txtVueAcount = (TextView) itemView.findViewById(R.id.txt_order_item_vue_acount);
            txtLblOdate = (TextView) itemView.findViewById(R.id.txt_order_item_lbl_odate);
            txtVueOdate = (TextView) itemView.findViewById(R.id.txt_order_item_vue_odate);
            btnTrush = (ImageButton) itemView.findViewById(R.id.imgbtn_order_item_trush);
            switch(listType){
                case OrderListPresenter.TYPE_ORDER_IN:
                    txtLblCont.setText(itemView.getResources().getString(R.string.str_order_item_lbl_cont_in));
                    txtLblAcount.setText(itemView.getResources().getString(R.string.str_order_item_lbl_acount_in));
                    txtLblOdate.setText(itemView.getResources().getString(R.string.str_order_item_lbl_odate_in));
                    break;
                case OrderListPresenter.TYPE_ORDER_OUT:
                    txtLblCont.setText(itemView.getResources().getString(R.string.str_order_item_lbl_cont_out));
                    txtLblAcount.setText(itemView.getResources().getString(R.string.str_order_item_lbl_acount_out));
                    txtLblOdate.setText(itemView.getResources().getString(R.string.str_order_item_lbl_odate_out));
                    break;
                case OrderListPresenter.TYPE_ORDER_REQ:
                    txtLblCont.setText(itemView.getResources().getString(R.string.str_order_item_lbl_cont_req));
                    txtLblAcount.setText(itemView.getResources().getString(R.string.str_order_item_lbl_acount_out));
                    txtLblOdate.setText(itemView.getResources().getString(R.string.str_order_item_lbl_odate_out));
                    break;
                case OrderListPresenter.TYPE_ORDER_RET:
                    txtLblCont.setText(itemView.getResources().getString(R.string.str_order_item_lbl_cont_ret));
                    txtLblAcount.setText(itemView.getResources().getString(R.string.str_order_item_lbl_acount_in));
                    txtLblOdate.setText(itemView.getResources().getString(R.string.str_order_item_lbl_odate_in));
                    break;
                case OrderListPresenter.TYPE_ORDER_INV:
                    txtLblCont.setText(itemView.getResources().getString(R.string.str_order_item_lbl_cont_inv));
                    txtLblMgr.setVisibility(View.GONE);
                    txtLblAcount.setText(itemView.getResources().getString(R.string.str_order_item_lbl_acount_inv));
                    txtLblOdate.setText(itemView.getResources().getString(R.string.str_order_item_lbl_odate_inv));
                    break;
            }
        }
    }
}

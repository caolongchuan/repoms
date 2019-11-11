package cn.reebtech.repoms.view;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.reebtech.repoms.R;

public class LoadingView extends DialogFragment {
    private ProgressBar pgsLoading;
    private TextView txtLoading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // DialogFragment如同AlertDialog一样，这段代码将取消标题，创建一个单纯的Frame
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 以下是该Fragment里面的内容
        View view = inflater.inflate(R.layout.loading_fragment, container);
        pgsLoading = view.findViewById(R.id.pgsLoading);
        txtLoading = view.findViewById(R.id.txtLoading);
        txtLoading.setText(getTag());

        return view;
    }
}

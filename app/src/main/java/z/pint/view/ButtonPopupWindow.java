package z.pint.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import z.pint.R;
import z.pint.utils.ViewUtils;

/**
 * Created by DN on 2018/6/22.
 */

public class ButtonPopupWindow extends PopupWindow implements View.OnClickListener{

    private LayoutInflater inflater;
    private String [] tabName={"拍照","相册","取消"};
    private OnSelectItemListener listener;

    public interface  OnSelectItemListener{
        void onSelectItemOnclick(int position,String tabName);
    }
    public ButtonPopupWindow(Context mContext, OnSelectItemListener listener){
        super(mContext);
        this.listener = listener;
        inflater = LayoutInflater.from(mContext);
        initView();
    }
    public ButtonPopupWindow(Context mContext, String [] tabName, OnSelectItemListener listener){
        super(mContext);
        this.listener = listener;
        this.tabName = tabName;
        inflater = LayoutInflater.from(mContext);
        initView();
    }


    private void initView() {
        View view = inflater.inflate(R.layout.photo_popupwindow_layout, null);
        //设置View
        setContentView(view);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置背景
        setBackgroundDrawable(new BitmapDrawable());
        //设置动画
        setAnimationStyle(f.base.R.style.anim_pw_button);
        //设置点击外边可以消失
        setOutsideTouchable(false);
        TextView txt0 = view.findViewById(R.id.txt0);
        TextView txt1 = view.findViewById(R.id.txt1);
        TextView pw_cancel = view.findViewById(R.id.pw_cancel);
        ViewUtils.setTextView(txt0,tabName[0]);
        ViewUtils.setTextView(txt1,tabName[1]);
        ViewUtils.setTextView(pw_cancel,tabName[2]);
        txt0.setOnClickListener(this);
        txt1.setOnClickListener(this);
        pw_cancel.setOnClickListener(this);
    }

    public void showPopupWindow(View view,int orientation,int pX,int pY){
        this.showAtLocation(view, orientation, pX, pY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt0:
                listener.onSelectItemOnclick(2,tabName[0]);
                break;
            case R.id.txt1:
                listener.onSelectItemOnclick(1,tabName[1]);
                break;
            case R.id.pw_cancel:
                listener.onSelectItemOnclick(0,tabName[2]);
                break;

        }
    }

}

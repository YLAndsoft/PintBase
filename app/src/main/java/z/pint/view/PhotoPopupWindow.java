package z.pint.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import z.pint.R;

/**
 * Created by DN on 2018/6/22.
 */

public class PhotoPopupWindow extends PopupWindow implements View.OnClickListener{

    private LayoutInflater inflater;

    private OnSelectItemListener listener;

    public interface  OnSelectItemListener{
        void onSelectItemOnclick(int position);
    }
    public PhotoPopupWindow(Context mContext,OnSelectItemListener listener){
        super(mContext);
        this.listener = listener;
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
        TextView photograph = view.findViewById(R.id.photograph);
        TextView album = view.findViewById(R.id.album);
        TextView pw_cancel = view.findViewById(R.id.pw_cancel);
        photograph.setOnClickListener(this);
        album.setOnClickListener(this);
        pw_cancel.setOnClickListener(this);
    }

    public void showPopupWindow(View view,int orientation,int pX,int pY){
        this.showAtLocation(view, orientation, pX, pY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.photograph:
                listener.onSelectItemOnclick(2);
                break;
            case R.id.album:
                listener.onSelectItemOnclick(1);
                break;
            case R.id.pw_cancel:
                listener.onSelectItemOnclick(0);
                break;

        }
    }

}

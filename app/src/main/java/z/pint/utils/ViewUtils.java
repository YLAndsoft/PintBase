package z.pint.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.xutils.common.util.LogUtil;

import f.base.utils.StringUtils;

/**
 * Created by DN on 2018/6/22.
 */

public class ViewUtils {

    public static RecyclerView.LayoutManager getLayoutManager(Context mContext){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }
    /**
     * 设置文本
     * @param view
     * @param result
     * @param defaultResult
     */
    public static void setTextView(View view,String result,String defaultResult){
        if(view instanceof TextView){
            if(!StringUtils.isBlank(result+"")){
                ((TextView) view).setText(result);
            }else{
                ((TextView) view).setText(defaultResult+"");
            }
        }else if(view instanceof EditText){
            if(!StringUtils.isBlank(result)){
                ((EditText) view).setHint(result+"");
            }else{
                ((EditText) view).setHint(defaultResult+"");
            }
        }else if(view instanceof Button){
            if(!StringUtils.isBlank(result)){
                ((Button) view).setText(result+"");
            }else{
                ((Button) view).setText(defaultResult+"");
            }
        }
    }

    /**
     * 设置图片
     * @param context
     * @param view
     * @param result
     * @param defaultResources
     */
    public static void setImageUrl(Context context,View view, Object result, int defaultResources){
        if(view instanceof ImageView){
            try{
                if(result!=null){
                    RequestManager rm = Glide.with(context);
                    if(result instanceof String){
                        rm.load(result+"").error(defaultResources).thumbnail(0.1f).centerCrop().into((ImageView) view);
                    }else if(result instanceof Bitmap){
                        rm.load((Bitmap)result).error(defaultResources).thumbnail(0.1f).centerCrop().into((ImageView) view);
                    }
                }else{
                    ((ImageView) view).setImageResource(defaultResources);
                }
            }catch (Exception ex){
                LogUtil.e("设置图片异常！");
                ex.printStackTrace();
            }
        }
    }
}

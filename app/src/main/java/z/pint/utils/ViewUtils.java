package z.pint.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import z.pint.R;

/**
 * Created by DN on 2018/6/22.
 */

public class ViewUtils {

    /**
     * 获取垂直布局管理器
     * @param mContext
     * @return
     */
    public static RecyclerView.LayoutManager getLayoutManager(Context mContext){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    /**
     * 获取水平布局管理器
     * @param mContext
     * @return
     */
    public static RecyclerView.LayoutManager getHorManager(Context mContext){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        return linearLayoutManager;
    }
    /**
     * 获取水平布局管理器
     * @param mContext
     * @return
     */
    public static RecyclerView.LayoutManager getStaggeredGridManager(Context mContext,int num){
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(num, StaggeredGridLayoutManager.VERTICAL);
        //RecyclerView滑动过程中不断请求layout的Request，不断调整item见的间隙，并且是在item尺寸显示前预处理，因此解决RecyclerView滑动到顶部时仍会出现移动问题
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        //内容随高度变化
        layoutManager.setAutoMeasureEnabled(true);
        return layoutManager;
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
                        rm.load(result+"").error(defaultResources).centerCrop().into((ImageView) view);
                    }else if(result instanceof Bitmap){
                        rm.load((Bitmap)result).error(defaultResources).centerCrop().into((ImageView) view);
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

    /**
     * 设置标签
     * @param label
     * @param label1
     * @param label2
     * @param label3
     */
    public static void setLabel(String label,TextView label1,TextView label2,TextView label3){
        if(StringUtils.isBlank(label)){return;}
        String[] split = label.split("\\|");
        if(null==split||split.length<=0){return;}
        switch (split.length){
            case 1:
                setTextView(label1,"# "+split[0],"# 原创");
                label1.setVisibility(View.VISIBLE);
                break;
            case 2:
                setTextView(label1,"# "+split[0],"");
                setTextView(label2,"# "+split[1],"");
                label1.setVisibility(View.VISIBLE);
                label2.setVisibility(View.VISIBLE);
                break;
            case 3:
                setTextView(label1,"# "+split[0],"");
                setTextView(label2,"# "+split[1],"");
                setTextView(label3,"# "+split[2],"");
                label1.setVisibility(View.VISIBLE);
                label2.setVisibility(View.VISIBLE);
                label3.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static void setSex(ImageView imageView,int sex){
        if(sex==0){
            imageView.setImageResource(R.mipmap.male);
            return;
        }
        imageView.setImageResource(R.mipmap.female);
    }
    /**
     * 设置是否关注
     * @param isAttention
     * @param imageview
     */
    public static boolean isAttention(boolean isAttention,ImageView imageview){
        if(isAttention){
            imageview.setImageResource(R.mipmap.unfollow);
            return isAttention;
        }
        imageview.setImageResource(R.mipmap.follow);
        return isAttention;
    }

    /**
     * 设置是否点赞
     * @param isLikes
     * @param imageview
     * @return
     */
    public static boolean isLikes(boolean isLikes,ImageView imageview){
        if(isLikes){
            imageview.setImageResource(R.mipmap.dynamic_love_hl);
            return isLikes;
        }
        imageview.setImageResource(R.mipmap.dynamic_love);
        return isLikes;
    }

    /**
     * 设置点赞数
     * @param isLikes
     * @param textView
     * @param number
     * @return
     */
    public static int setLikesNumber(boolean isLikes,TextView textView,int number){
        if(isLikes){
            if(null!=textView)textView.setText((number+1)+"");
            return number+1;
        }else{
            if(null!=textView)textView.setText((number-1)<=0?0+"":(number-1)+"");
            return (number-1)<=0?0:(number-1);
        }
    }

}

package z.pint.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import f.base.BaseRecyclerHolder;
import z.pint.R;
import z.pint.activity.WorksDetailsActivity;
import z.pint.bean.Works;
import z.pint.utils.ViewUtils;

/**
 * 作品搜索适配器
 * Created by DN on 2018/7/14.
 */

public class SearchWorkAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Works> list;
    private LayoutInflater inflater;
    public SearchWorkAdapter(Context mContext, List<Works> list){
        this.mContext = mContext;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recommend_item_layout, parent, false);
        return new SearchWorkHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof SearchWorkHolder){
            SearchWorkHolder searchHolder = (SearchWorkHolder) holder;
            Glide.with(mContext).load(list.get(position).getWorksImage()).thumbnail(1f).into(searchHolder.recommend_item_image);
            Glide.with(mContext).load(list.get(position).getUserHead()).centerCrop().into(searchHolder.recommend_item_userhead);
            ViewUtils.setTextView(searchHolder.recommend_item_username,list.get(position).getUserName());
            ViewUtils.setTextView(searchHolder.recommend_item_des,list.get(position).getWorksDescribe());
            searchHolder.recommend_item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WorksDetailsActivity.class);
                    intent.putExtra("works",list.get(position));
                    mContext.startActivity(intent);
                }
            });
            searchHolder.recommend_item_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WorksDetailsActivity.class);
                    intent.putExtra("works",list.get(position));
                    mContext.startActivity(intent);
                }
            });
            searchHolder.recommend_item_likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showToast("点赞");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list!=null&&list.size()>0?list.size():0;
    }

    /**
     * 刷新所有数据
     * @param works
     */
    public void refreshAll(List<Works> works) {
        this.list = null;
        this.list=works;
        notifyDataSetChanged();
    }
    /**
     * 添加所有数据
     * @param works
     */
    public void addAll(List<Works> works) {
        this.list.addAll(works);
        notifyItemRangeChanged(works.size(),0);
    }


    private class SearchWorkHolder extends RecyclerView.ViewHolder {
        @ViewInject(value = R.id.recommend_item_image)
        private ImageView recommend_item_image;
        @ViewInject(value = R.id.recommend_item_userhead)
        private ImageView recommend_item_userhead;
        @ViewInject(value = R.id.recommend_item_username)
        private TextView recommend_item_username;
        @ViewInject(value = R.id.recommend_item_des)
        private TextView  recommend_item_des;
        @ViewInject(value = R.id.recommend_item_likes)
        private ImageView  recommend_item_likes;
        public SearchWorkHolder(View view) {
            super(view);
            x.view().inject(this,view);
        }
    }
}

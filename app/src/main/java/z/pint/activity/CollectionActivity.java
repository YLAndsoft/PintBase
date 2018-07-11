package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import f.base.BaseActivity;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import z.pint.R;
import z.pint.bean.Collection;
import z.pint.utils.DBHelper;
import z.pint.utils.SPUtils;
import z.pint.utils.TimeUtils;
import z.pint.utils.ViewUtils;

/**
 * Created by DN on 2018/7/4.
 */

public class CollectionActivity extends BaseActivity {
    @ViewInject(value = R.id.collection_recycler)
    private RecyclerView collection_recycler;
    @ViewInject(value = R.id.default_titleName)
    private TextView default_titleName;
    private int userID;
    private List<Collection> collections;
    private BaseRecyclerAdapter<Collection> adapter;
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_collection;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this);
    }
    @Override
    public void initListener() {
    }

    @Override
    public void initData(final Context mContext) {
        ViewUtils.setTextView(default_titleName,getResources().getString(R.string.collection_titelName),"");
        collection_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        userID = (int) SPUtils.getInstance(mContext).getParam("userID",0);
        //查询数据库，得到收藏作品数据
        collections = DBHelper.selectCollectionAll(userID);
        if(null==collections||collections.size()<0){return;}
        sortList(collections);
        adapter = new BaseRecyclerAdapter<Collection>(mContext,collections,R.layout.collection_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, Collection item, int position) {
                ImageView home_item_works_img = holder.getView(R.id.collection_works_img);
                Glide.with(mContext).load(item.getWorksImag()).placeholder(R.mipmap.img_placeholder).thumbnail(0.1f).into(home_item_works_img);
                holder.setText(R.id.collection_time,"收藏于"+item.getCollectionTime());
                ImageView coll_userHead = holder.getView(R.id.collection_works_userHead);
                Glide.with(mContext).load(item.getUserHead()).centerCrop().into(coll_userHead);
                holder.setText(R.id.collection_works_userName,item.getUserName());
                holder.setText(R.id.worksReleaseTime,item.getWorksReleaseTime());
                holder.setText(R.id.collection_des,item.getWorksDescribe());
                holder.setText(R.id.collection_commentNumber,item.getWorksCommentNumber()+"");
                holder.setText(R.id.collection_likesNumber,item.getWorksLikeNumber()+"");
            }
        };
        collection_recycler.setAdapter(adapter);
    }

    @Override
    public void widgetClick(View v) {

    }
    /**
     * 时间排序来显示
     * @param collList
     */
    private void sortList(List<Collection> collList) {
        Comparator<Collection> itemComparator = new Comparator<Collection>() {
            public int compare(Collection info1, Collection info2){
                Date data1 = TimeUtils.stringToDate(info1.getCollectionTime(),"yyyy-MM-dd HH:mm:ss");
                Date data2 = TimeUtils.stringToDate(info2.getCollectionTime(),"yyyy-MM-dd HH:mm:ss");
                return data2.compareTo(data1);
            }
        };
        Collections.sort(collList, itemComparator);
    }


    @Override
    public Params getParams() {
        return null;
    }

    @Override
    protected void setData(String result) {
    }
}

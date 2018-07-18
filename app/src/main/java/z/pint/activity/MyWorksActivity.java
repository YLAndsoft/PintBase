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
import z.pint.bean.Works;
import z.pint.utils.DBHelper;
import z.pint.utils.TimeUtils;
import z.pint.utils.ViewUtils;

/**
 * 我的作品界面
 * Created by DN on 2018/7/10.
 */

public class MyWorksActivity extends BaseActivity {

    @ViewInject(value = R.id.default_titleName)
    private TextView default_titleName;
    @ViewInject(value = R.id.my_works_recycler)
    private RecyclerView my_works_recycler;
    private BaseRecyclerAdapter<Works> adapter;
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_my_works;
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
        default_titleName.setText(getResources().getString(R.string.works_list));

        my_works_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        List<Works> worksList = DBHelper.selectWorksAll();
        if(null==worksList||worksList.size()<=0){return;}
        sortList(worksList);
        adapter = new BaseRecyclerAdapter<Works>(mContext,worksList,R.layout.my_works_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, final Works item, int position) {
                holder.setText(R.id.my_works_userName,item.getUserName()+"");
                ImageView home_item_works_img = holder.getView(R.id.my_works_img);
                Glide.with(mContext).load(item.getWorksImage()).placeholder(R.mipmap.img_placeholder).thumbnail(0.1f).into(home_item_works_img);
                ImageView coll_userHead = holder.getView(R.id.my_works_userHead);
                Glide.with(mContext).load(item.getUserHead()).centerCrop().into(coll_userHead);
                holder.setText(R.id.my_works_userName,item.getUserName());
                holder.setText(R.id.worksReleaseTime,item.getWorksReleaseTime());
                holder.setText(R.id.my_des,item.getWorksDescribe());
                holder.setText(R.id.my_commentNumber,item.getWorksCommentNumber()+"");
                holder.setText(R.id.my_likesNumber,item.getWorksLikeNumber()+"");
                holder.setOnViewClick(R.id.my_work_userinfo, item, position, new BaseRecyclerHolder.OnViewClickListener() {
                    @Override
                    public void onViewClick(View view, Object object, int position) {
                        Intent userinfo = new Intent(mContext, UserInfoActivity.class);
                        userinfo.putExtra("userID",item.getUserID()+"");
                        startActivity(userinfo);
                    }
                });
                holder.setOnViewClick(R.id.my_works_img, item, position, new BaseRecyclerHolder.OnViewClickListener() {
                    @Override
                    public void onViewClick(View view, Object object, int position) {
                        Intent intent = new Intent(mContext, WorksDetailsActivity.class);
                        intent.putExtra("works",(Works)object);
                        intent.putExtra("position",position);
                        startActivity(intent);
                    }
                });
            }
        };
        my_works_recycler.setAdapter(adapter);
    }
    /**
     * 时间排序来显示
     * @param collList
     */
    private void sortList(List<Works> collList) {
        Comparator<Works> itemComparator = new Comparator<Works>() {
            public int compare(Works info1, Works info2){
                Date data1 = TimeUtils.stringToDate(info1.getWorksReleaseTime(),"yyyy-MM-dd HH:mm:ss");
                Date data2 = TimeUtils.stringToDate(info2.getWorksReleaseTime(),"yyyy-MM-dd HH:mm:ss");
                return data2.compareTo(data1);
            }
        };
        Collections.sort(collList, itemComparator);
    }
    @Override
    public void widgetClick(View v) {

    }
    @Override
    public Params getParams() {
        return null;
    }
    @Override
    protected void setData(String result) {

    }


}

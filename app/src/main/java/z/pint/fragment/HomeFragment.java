package z.pint.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import f.base.BaseFragment;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import z.pint.R;
import z.pint.bean.Works;

/**
 * Created by DN on 2018/6/19.
 */

public class HomeFragment extends BaseFragment {
    @ViewInject(value = R.id.home_recycler)
    private RecyclerView home_recycler;

    @Override
    public int bindLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        home_recycler.setLayoutManager(linearLayoutManager);
        List<Works> works = new ArrayList<>();
        for(int i=0;i<10;i++){
            works.add(new Works());
        }
        BaseRecyclerAdapter<Works> adapter = new BaseRecyclerAdapter<Works>(mContext,works,R.layout.home_recycler_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, Works works, int i) {
                ImageView view = baseRecyclerHolder.getView(R.id.home_item_works_img);
                Glide.with(mContext).load(R.mipmap.ova).thumbnail(0.1f).centerCrop().into(view);
            }
        };
        home_recycler.setAdapter(adapter);

    }

    @Override
    public void widgetClick(View view) {

    }


}

package z.pint.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import f.base.BaseFragment;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import z.pint.R;

/**
 * Created by DN on 2018/6/21.
 */

public class UserWorksFragment extends BaseFragment {

    @ViewInject(value = R.id.userinfo_works_recycler)
    private RecyclerView userinfo_works_recycler;
    @Override
    public int bindLayout() {
        return R.layout.fragment_userinfo_works;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add("第"+i+"条数据");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userinfo_works_recycler.setLayoutManager(linearLayoutManager);
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(mContext,list,R.layout.userinfo_works_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, String s, int i) {
                baseRecyclerHolder.setText(R.id.comment_content,s+"");
            }
        };
        userinfo_works_recycler.setAdapter(adapter);
    }

    @Override
    public void widgetClick(View view) {

    }

    @Override
    public Params getParams() {
        return null;
    }

    @Override
    protected void setData(String s) {

    }

}

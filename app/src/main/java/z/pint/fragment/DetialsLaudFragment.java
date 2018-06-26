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
 * Created by DN on 2018/6/26.
 */

public class DetialsLaudFragment extends BaseFragment {

    @ViewInject(value = R.id.details_laud_recycler)
    RecyclerView details_laud_recycler;
    @Override
    public int bindLayout() {
        return R.layout.details_laud_fragment;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add("第"+i+"条点赞");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        details_laud_recycler.setLayoutManager(linearLayoutManager);
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(mContext,list,R.layout.userinfo_works_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, String s, int i) {
                baseRecyclerHolder.setText(R.id.userinfo_works,s+"");
            }
        };
        details_laud_recycler.setAdapter(adapter);
    }

    @Override
    public void widgetClick(View view) {

    }

    @Override
    public Params getParams() { //设置网络请求参数及地址
        return null;
    }

    @Override
    protected void setData(String s) {
        //绑定网络数据
    }

}

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
import z.pint.R;

/**
 * 用户信息里面个人页面
 * Created by DN on 2018/6/21.
 */

public class PersonalFragment extends BaseFragment{

    @ViewInject(value = R.id.userinfo_personalRecycler)
    private RecyclerView userinfo_personalRecycler;

    @Override
    public int bindLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        list.add("123456");
        list.add("还没有签名");
        list.add("湖北武汉");
        list.add("2018-06-21 14:04:33");
        final String ss [] = {"ID:","签名:","所在地:","注册时间:"};
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userinfo_personalRecycler.setLayoutManager(linearLayoutManager);

        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(mContext,list,R.layout.userinfo_personal_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, String s, int i) {
                baseRecyclerHolder.setText(R.id.personal_txt,ss[i]+"");
                baseRecyclerHolder.setText(R.id.personal_content,s+"");
            }
        };
        userinfo_personalRecycler.setAdapter(adapter);


    }

    @Override
    public void widgetClick(View view) {

    }
}

package z.pint.fragment;

import android.os.Bundle;
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
import z.pint.bean.User;
import z.pint.utils.ViewUtils;

/**
 * 用户信息里面个人页面
 * Created by DN on 2018/6/21.
 */

public class PersonalFragment extends BaseFragment{

    @ViewInject(value = R.id.userinfo_personalRecycler)
    private RecyclerView userinfo_personalRecycler;
    private User userInfo;
    private String [] name;
    @Override
    public int bindLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }
    @Override
    public Params getParams() {
        return null;
    }

    @Override
    protected void setData(Object object,boolean isRefresh) {

    }

    @Override
    protected void showError(String result) {

    }
    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        userInfo = (User) bundle.getSerializable("userInfo");
        name = getString();
        userinfo_personalRecycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(mContext,getListInfo(userInfo),R.layout.userinfo_personal_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, String s, int i) {
                baseRecyclerHolder.setText(R.id.personal_txt,name[i]+"");
                baseRecyclerHolder.setText(R.id.personal_content,s+"");
            }
        };
        userinfo_personalRecycler.setAdapter(adapter);
    }

    @Override
    public void widgetClick(View view) {

    }

    private String[] getString(){
        String ss [] = {getResources().getString(R.string.ID),
                getResources().getString(R.string.SIGN),
                getResources().getString(R.string.ADDRESS),
                getResources().getString(R.string.REGISTRTIME)};
        return ss;
    }
    private List<String> getListInfo(User user){
        if(user==null){
            return null;
        }
        List<String> list = new ArrayList<>();
        list.add(user.getUserID()+"");
        list.add(user.getUserSign());
        list.add(user.getUserAddress());
        list.add(user.getRegistrTime());
        return list;
    }

}

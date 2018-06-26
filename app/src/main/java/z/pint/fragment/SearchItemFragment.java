package z.pint.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.BaseFragment;
import f.base.bean.Params;
import z.pint.R;

/**
 * Created by DN on 2018/6/19.
 */

public class SearchItemFragment extends BaseFragment {
    @ViewInject(value = R.id.text)
    private TextView text;

    @Override
    public int bindLayout() {
        return R.layout.fragment_search_item_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        String classifyName = bundle.getString("classifyName");
        text.setText(classifyName+"");
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

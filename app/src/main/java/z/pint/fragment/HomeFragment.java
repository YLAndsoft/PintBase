package z.pint.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.BaseFragment;
import z.pint.R;

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

    }

    @Override
    public void widgetClick(View view) {

    }
}

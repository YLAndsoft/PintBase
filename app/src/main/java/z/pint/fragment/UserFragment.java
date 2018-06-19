package z.pint.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.BaseFragment;
import z.pint.R;

/**
 * Created by DN on 2018/6/19.
 */

public class UserFragment extends BaseFragment {
    @ViewInject(value = R.id.rl_user)
    RelativeLayout rl_user;

    @Override
    public int bindLayout() {
        return R.layout.fragment_user_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);

        rl_user.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.rl_user:
                showToast("跳转到个人信息界面");
                break;
        }
    }
}

package z.pint.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import f.base.BaseFragment;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.view.PagerSlidingTabStrip;

/**
 * Created by DN on 2018/6/19.
 */

public class SearchFragment extends BaseFragment {
    @ViewInject(value = R.id.new_search_tabs)
    private PagerSlidingTabStrip new_search_tabs;
    @ViewInject(value = R.id.message_pager)
    private ViewPager message_pager;
    @ViewInject(value = R.id.new_search)
    private ImageView new_search;

    private List<Fragment> fragments = new ArrayList<>();
    private ClassifyViewPagerAdapter classViewPagerAdapter;

    String tabName []= {"最新","推荐","二次元","插画","古风","写实","国画","风景"};
    @Override
    public int bindLayout() {
        return R.layout.fragment_search_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        new_search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        for(int i=0;i<tabName.length;i++){
            list.add(tabName[i]);
            SearchItemFragment sif = new SearchItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("classifyName", tabName[i]);
            sif.setArguments(bundle);
            fragments.add(sif);
        }
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getFragmentManager(), list, fragments);
        message_pager.setAdapter(classViewPagerAdapter);
        message_pager.setOffscreenPageLimit(tabName.length);//依据传过来的tab页的个数来设置缓存的页数
        //tabs.setFollowTabColor(true);//设置标题是否跟随
        new_search_tabs.setViewPager(message_pager);
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.new_search:
                showToast("点击了搜索");
                break;
        }
    }


}

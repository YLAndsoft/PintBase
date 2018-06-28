package z.pint.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseFragment;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.bean.WorksClassify;
import z.pint.constant.HttpConfig;
import z.pint.view.PagerSlidingTabStrip;

/**
 * Created by DN on 2018/6/19.
 */

public class RecommendFragment extends BaseFragment {
    @ViewInject(value = R.id.new_search_tabs)
    private PagerSlidingTabStrip new_search_tabs;
    @ViewInject(value = R.id.recommend_pager)
    private ViewPager recommend_pager;
    @ViewInject(value = R.id.new_search)
    private ImageView new_search;

    private ClassifyViewPagerAdapter classViewPagerAdapter;
    private int start=0;
    private int num = 10;

    @Override
    public int bindLayout() {
        return R.layout.fragment_recommend_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        new_search.setOnClickListener(this);
    }
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.APPNAME, getResources().getString(R.string.http_name) + "");
        //new Params(HttpConfig.getRecommendData,map);
        return new Params(HttpConfig.getClassifyNameData, map);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setData(String result) {
        if(StringUtils.isBlank(result)){return;}
        List<WorksClassify> classifyName = GsonUtils.getGsonList(result, WorksClassify.class);
        if(null==classifyName||classifyName.size()<=0){
            return;
        }
        List<String> listTabName = getListTabName(classifyName);
        List<Fragment> listFragment = getListFragment(classifyName);
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getFragmentManager(), listTabName, listFragment);
        recommend_pager.setAdapter(classViewPagerAdapter);
        recommend_pager.setOffscreenPageLimit(classifyName.size());//依据传过来的tab页的个数来设置缓存的页数
        //tabs.setFollowTabColor(true);//设置标题是否跟随
        new_search_tabs.setViewPager(recommend_pager);
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.new_search:
                showToast("点击了搜索");
                break;
        }
    }

    private List<Fragment> getListFragment(List<WorksClassify> classifyName){
        List<Fragment> listFragments = new ArrayList<>();
        for(int i=0;i<classifyName.size();i++){
            RecommendItemFragment rif = new RecommendItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("classifyID", classifyName.get(i).getClassifyID()+"");
            rif.setArguments(bundle);
            listFragments.add(rif);
        }
        return listFragments;
    }
    private List<String> getListTabName(List<WorksClassify> classifyName){
        List<String> list = new ArrayList<>();
        for(int i=0;i<classifyName.size();i++){
            list.add(classifyName.get(i).getClassifyName());
        }
        return list;
    }



}

package f.base.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import f.base.R;
import f.base.bean.CIModel;
import f.base.bean.DIModel;
import f.base.bean.PIModel;
import f.base.utils.AXParser;
import f.base.utils.StringUtils;

/**
 * Created by DN on 2018/6/15.
 */

public class ProvincePopupWindow extends PopupWindow implements View.OnClickListener {

    private ArrayList<String> mProvinceDatas = new ArrayList<String>();
    private Map<String, ArrayList<String>> mCitisDatasMap = new HashMap<String, ArrayList<String>>();
    private Map<String, ArrayList<String>> mDistrictDatasMap = new HashMap<String, ArrayList<String>>();
    private Context mContext;
    private String mCurrentProviceName="";
    private String mCurrentCityName="";
    private String mCurrentDistrictName="";

    private WView mProvincePicker;
    private WView mCityPicker;
    private WView mCountyPicker;

    public interface OnResultClickListener{
        void onResultClick(String result);
    }
    private OnResultClickListener listener;
    public void setListener(OnResultClickListener listener) {
        this.listener = listener;
    }

    /**
     * @param context
     * @param addressXML 保存在assets目录下的文件名称
     */
    public ProvincePopupWindow(Context context,String addressXML){
        super(context);
        this.mContext = context;

        initDataAddress(mContext,addressXML);//初始化地区文件

        initView();//初始化布局

        initData();//初始化数据

    }

    private void initView() {
        View pView = LayoutInflater.from(mContext).inflate(R.layout.address_layout,null);
        //设置View
        setContentView(pView);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置背景
        setBackgroundDrawable(new BitmapDrawable());
        //设置动画
        setAnimationStyle(R.style.anim_pw_button);
         //设置点击外边可以消失
        setOutsideTouchable(true);
        //设置可以获取集点
        //setFocusable(true);

        mProvincePicker = (WView) pView.findViewById(R.id.province);
        mCityPicker = (WView) pView.findViewById(R.id.city);
        mCountyPicker = (WView) pView.findViewById(R.id.county);
        TextView picker_sure = (TextView) pView.findViewById(R.id.picker_sure);
        TextView picker_cancel = (TextView) pView.findViewById(R.id.picker_cancel);

        mProvincePicker.setOnSelectListener(new WView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if(null!=mProvinceDatas&&mProvinceDatas.size()>0){
                    String provinceText = mProvinceDatas.get(id);
                    if (!mCurrentProviceName.equals(provinceText)) {
                        mCurrentProviceName = provinceText;
                        ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                        mCityPicker.resetData(mCityData);
                        mCityPicker.setDefault(0);
                        mCurrentCityName = mCityData.get(0);
                        ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                        mCountyPicker.resetData(mDistrictData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mDistrictData.get(0);
                    }
                }
            }
            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if(null!=mProvinceDatas&&mProvinceDatas.size()>0){
                    ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                    String cityText = mCityData.get(id);
                    if (!mCurrentCityName.equals(cityText)) {
                        mCurrentCityName = cityText;
                        ArrayList<String> mCountyData = mDistrictDatasMap.get(mCurrentCityName);
                        mCountyPicker.resetData(mCountyData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mCountyData.get(0);
                    }
                }

            }
            @Override
            public void selecting(int id, String text) {
            }
        });
        mCountyPicker.setOnSelectListener(new WView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if(null!=mProvinceDatas&&mProvinceDatas.size()>0){
                    ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                    String districtText = mDistrictData.get(id);
                    if (!mCurrentDistrictName.equals(districtText)) {
                        mCurrentDistrictName = districtText;
                    }
                }
            }
            @Override
            public void selecting(int id, String text) {
            }
        });
        picker_sure.setOnClickListener(this);
        picker_cancel.setOnClickListener(this);
    }

    private void initData() {
        if(null!=mProvinceDatas&&mProvinceDatas.size()>0){
            mProvincePicker.setData(mProvinceDatas);
            mProvincePicker.setDefault(0);
            mCurrentProviceName = mProvinceDatas.get(0);
        }
        if(null!=mCitisDatasMap&&mCitisDatasMap.size()>0){
            ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
            mCityPicker.setData(mCityData);
            mCityPicker.setDefault(0);
            mCurrentCityName = mCityData.get(0);
        }
        if(null!=mDistrictDatasMap&&mDistrictDatasMap.size()>0){
            ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
            mCountyPicker.setData(mDistrictData);
            mCountyPicker.setDefault(0);
            mCurrentDistrictName = mDistrictData.get(0);
        }

    }

    private void initDataAddress(Context mContext, String addressXML) {
        List<PIModel> provinceList = null;
        if(StringUtils.isBlank(addressXML))return;
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open(addressXML);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            AXParser handler = new AXParser();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CIModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DIModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                }
            }
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas.add(provinceList.get(i).getName());
                List<CIModel> cityList = provinceList.get(i).getCityList();
                ArrayList<String> cityNames = new ArrayList<String>();
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames.add(cityList.get(j).getName());
                    List<DIModel> districtList = cityList.get(j).getDistrictList();
                    ArrayList<String> distrinctNameArray = new ArrayList<String>();
                    DIModel[] distrinctArray = new DIModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DIModel districtModel = new DIModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray.add(districtModel.getName());
                    }
                    mDistrictDatasMap.put(cityNames.get(j), distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void showPopupWindow(View view,int orientation,int pX,int pY){
        this.showAtLocation(view, orientation, pX, pY);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.picker_sure){
            String address = mCurrentProviceName + mCurrentCityName;
            if (!mCurrentDistrictName.equals("其他")) {
                address += mCurrentDistrictName;
            }
            listener.onResultClick(address);
            this.dismiss();
        }else if(view.getId()==R.id.picker_cancel){
            listener.onResultClick("");
            this.dismiss();
        }
    }

}

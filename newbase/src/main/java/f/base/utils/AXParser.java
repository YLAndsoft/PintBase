package f.base.utils;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import f.base.bean.CIModel;
import f.base.bean.DIModel;
import f.base.bean.PIModel;

/**
 * Created by DN on 2018/6/15.
 */

public class AXParser extends DefaultHandler {
    private List<PIModel> provinceList = new ArrayList<PIModel>();
    PIModel provinceModel ;
    CIModel cityModel;
    DIModel districtModel;
    public java.util.List<PIModel> getDataList() {
        return provinceList;
    }
    @Override
    public void startDocument() throws SAXException {
    }
    @Override
    public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
        if (qName.equals("province")) {
            provinceModel = new PIModel();
            provinceModel.setName(attributes.getValue(0));
            provinceModel.setCityList(new ArrayList<CIModel>());
        } else if (qName.equals("city")) {
            cityModel = new CIModel();
            cityModel.setName(attributes.getValue(0));
            cityModel.setDistrictList(new ArrayList<DIModel>());
        } else if (qName.equals("district")) {
            districtModel = new DIModel();
            districtModel.setName(attributes.getValue(0));
            districtModel.setZipcode(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("district")) {
            cityModel.getDistrictList().add(districtModel);
        } else if (qName.equals("city")) {
            provinceModel.getCityList().add(cityModel);
        } else if (qName.equals("province")) {
            provinceList.add(provinceModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }
}

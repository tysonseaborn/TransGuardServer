package com.example.tyson.transguardserver;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Content implements Serializable {

    private List<String> registration_ids;
    private Map<String,String> data;

    // Instantiate the parser
    XMLParser xmlParser = new XMLParser();
    //InputStream inputStream = null;
    List<XMLParser.Entry> entries = null;
    AssetManager am;
    InputStream is;
    int xmlCounter = 1;
    Context context;


    public void addRegId(String regId){
        if(registration_ids == null)
            registration_ids = new LinkedList<String>();
        registration_ids.add(regId);
    }

    public void createData(Context context){
        if(data == null)
            data = new HashMap<String,String>();

        try {
            am = context.getAssets();
            is = am.open("xmlTestFile.xml");
            //is = new FileInputStream("C:/Users/Tyson/Desktop/xmlTestFile.xml");
            entries = xmlParser.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (XMLParser.Entry entry : entries) {
            if(entry.date != null) {
                data.put("date" + String.valueOf(xmlCounter), entry.date);
            }
            data.put("name" + String.valueOf(xmlCounter), entry.name);
            data.put("amount" + String.valueOf(xmlCounter), entry.amount);

            xmlCounter++;
        }

        //data.put("title", title);
        //data.put("message", message);
    }


    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
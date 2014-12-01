package com.example.tyson.transguardserver;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Content implements Serializable {

    private List<String> registration_ids;
    private Map<String,String> data;

    // Instantiate the parser
    XMLParser xmlParser = new XMLParser();

    List<XMLParser.Entry> entries = null;
    AssetManager am;
    InputStream is;
    int xmlCounter = 1;


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
            entries = xmlParser.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Format date and amount strings before server sends them to application
        Date newDate;
        String newDateStr = null;
        int newAmount;
        String stringAmount;

        for (XMLParser.Entry entry : entries) {

            data.put("name" + String.valueOf(xmlCounter), entry.name);

            if(entry.date != null) {
                try {
                    newDate = new SimpleDateFormat("0yyyyMMdd").parse(entry.date);
                    SimpleDateFormat postFormater = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                    newDateStr = postFormater.format(newDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                data.put("date" + String.valueOf(xmlCounter), newDateStr);
            }


            newAmount = Integer.parseInt(entry.amount);
            stringAmount = '$' + String.valueOf(newAmount);

            data.put("amount" + String.valueOf(xmlCounter), stringAmount);
            data.put("transactionSize", String.valueOf(entries.size()));

            xmlCounter++;
        }

    }
}
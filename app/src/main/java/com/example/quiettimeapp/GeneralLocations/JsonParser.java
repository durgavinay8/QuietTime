/*
package com.example.quiettimeapp.GeneralLocations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    private static HashMap<String, String> parseJsonObject(JSONObject object){
        //intialize hash map
        HashMap<String, String> dataList= new HashMap();
        try{
            String name=object.getString("name");
            //get latitude
            String latitude=object.getJSONObject("geometry")
                .getJSONObject("location").getString("lat");
            //get longitude
            String longitude=object.getJSONObject("geometry")
                .getJSONObject("location").getString("lng");
            dataList.put("name",name);
            dataList.put("lat",latitude);
            dataList.put("lon",longitude);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        //return hashmap
        return dataList;
    }
    private static List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray){
        //intialize hashmap list
        List<HashMap<String,String>>dataList=new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){
            //intialize hashmap
            try{
                HashMap<String,String>data=parseJsonObject((JSONObject) jsonArray.get(i));
                //add data in hashmap list
                dataList.add(data);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        //return hash maplist
        return dataList;

    }
    public static List<HashMap<String,String>>parseResult(JSONObject object){
        //intialize json array list
        JSONArray jsonArray=null;
        //get result array
        try{
            jsonArray=object.getJSONArray("results");

        }catch(JSONException e){
            e.printStackTrace();
        }
        //return array
        List<HashMap<String,String>> l = parseJsonArray(jsonArray);

        for (HashMap<String, String> map : l) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key + " : " + value);
            }
            System.out.println("---"); // Separator between hashmaps
        }

        return l;
    }
}
*/

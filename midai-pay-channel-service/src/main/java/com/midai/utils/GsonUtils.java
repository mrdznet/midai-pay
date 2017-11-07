package com.midai.utils;



import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GoldMidai on 2016/12/2.
 */

public class GsonUtils {


    public static Gson sGson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 把一个map变成json字符串
     * @param map
     * @return
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            return sGson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把一个json字符串变成对象
     * @param json
     * @param cls
     * @return
     */
    public static <T> T fromJson(String json, Class<T> cls) {

        return sGson.fromJson(json, cls);
    }


    /**
     * 把一个json字符串变成对象
     * @param json
     * @param type
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        return sGson.fromJson(json, type);
    }
    /**
     * 把一个对象变成json
     * @return
     */
    public static String toJson(Object o) {

        return sGson.toJson(o);
    }

    /**
     * 把json字符串变成map
     * @param json
     * @return
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {

        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> map = null;
        try {
            map = sGson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type  new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static <T> List<T> parseJsonToList(String json, Class<T> type) {

        return sGson.fromJson(json, new TypeToken<List<T>>(){}.getType());
    }
    /**
     * 把json字符串变成集合
     */
    public static <T> List<T> getObjectList(String jsonString, Class<T> cls){
        List<T> list = new ArrayList<T>();
        try {

            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(sGson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

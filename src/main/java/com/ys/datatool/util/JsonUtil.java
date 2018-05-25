package com.ys.datatool.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by mo on  2017-09-18.
 */
public class JsonUtil {

    /**
     * FastJSON的序列化设置
     */
    private static SerializerFeature[] features = new SerializerFeature[]{
            //输出Map中为Null的值
            SerializerFeature.WriteMapNullValue,

            //如果Boolean对象为Null，则输出为false
            SerializerFeature.WriteNullBooleanAsFalse,

            //如果List为Null，则输出为[]
            SerializerFeature.WriteNullListAsEmpty,

            //如果Number为Null，则输出为0
            SerializerFeature.WriteNullNumberAsZero,

            //输出Null字符串
            SerializerFeature.WriteNullStringAsEmpty,

            //格式化输出日期
            SerializerFeature.WriteDateUseDateFormat
    };

    private static String toJSONString(Object obj){
        return JSON.toJSONString(obj, features);
    }

    public static void exportJson(HttpServletResponse response, Object data, String encoding){
        //设置编码格式
        response.setContentType("text/plain;charset=" + encoding);
        response.setCharacterEncoding(encoding);

        PrintWriter out = null;
        try{
            out = response.getWriter();
            out.write(toJSONString(data));
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void writeJsonToFile(String json, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] bytes = json.getBytes(); //新加的
        int b = json.length();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String beanToJson(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class)
                    .getPropertyDescriptors();
        } catch (IntrospectionException e) {
        }
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = objectToJson(props[i].getName());
                    String value = objectToJson(props[i].getReadMethod().invoke(bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                }
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    public static String objectToJson(Object object) {
        StringBuilder json = new StringBuilder();
        if (object == null) {
            json.append("\"\"");
        } else if (object instanceof String || object instanceof Integer) {
            json.append("\"").append(object.toString()).append("\"");
        } else {
            json.append(beanToJson(object));
        }
        return json.toString();
    }

    public static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(objectToJson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }


    public static String simpleListToJsonStr(List<?> list,List<Class> claList) throws IllegalArgumentException, IllegalAccessException{
        if(list==null||list.size()==0){
            return "[]";
        }
        String jsonStr = "[";
        for (Object object : list) {
            jsonStr += simpleObjectToJsonStr(object,claList)+",";
        }
        jsonStr = jsonStr.substring(0,jsonStr.length()-1);
        jsonStr += "]";
        return jsonStr;
    }


    public static String simpleObjectToJsonStr(Object obj, List<Class> claList) throws IllegalArgumentException, IllegalAccessException {
        if (obj == null) {
            return "null";
        }
        String jsonStr = "{";
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == long.class) {
                jsonStr += "\"" + field.getName() + "\":" + field.getLong(obj) + ",";
            } else if (field.getType() == double.class) {
                jsonStr += "\"" + field.getName() + "\":" + field.getDouble(obj) + ",";
            } else if (field.getType() == float.class) {
                jsonStr += "\"" + field.getName() + "\":" + field.getFloat(obj) + ",";
            } else if (field.getType() == int.class) {
                jsonStr += "\"" + field.getName() + "\":" + field.getInt(obj) + ",";
            } else if (field.getType() == boolean.class) {
                jsonStr += "\"" + field.getName() + "\":" + field.getBoolean(obj) + ",";
            } else if (field.getType() == Integer.class || field.getType() == Boolean.class
                    || field.getType() == Double.class || field.getType() == Float.class
                    || field.getType() == Long.class) {
                jsonStr += "\"" + field.getName() + "\":" + field.get(obj) + ",";
            } else if (field.getType() == String.class) {
                jsonStr += "\"" + field.getName() + "\":\"" + field.get(obj) + "\",";
            } else if (field.getType() == List.class) {
                String value = simpleListToJsonStr((List<?>) field.get(obj), claList);
                jsonStr += "\"" + field.getName() + "\":" + value + ",";
            } else {
                if (claList != null && claList.size() != 0 && claList.contains(field.getType())) {
                    String value = simpleObjectToJsonStr(field.get(obj), claList);
                    jsonStr += "\"" + field.getName() + "\":" + value + ",";
                } else {
                    jsonStr += "\"" + field.getName() + "\":null,";
                }
            }
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        jsonStr += "}";
        return jsonStr;
    }
}

package com.ys.datatool.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 2019/4/26
 */
public class DataUtil {

    public static <T> List<List<T>> split(List<T> resList, int count) {

        if (resList == null || count < 1)
            return null;
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }

    public static String formatString(String target) {

        if (target != null && target.contains("\\")) {
            target = target.replace("\\", "\\\\");
        }

        if (target != null && target.contains("\'")) {
            target = target.replace("\'", "\\'");
        }

        return target;

    }
}

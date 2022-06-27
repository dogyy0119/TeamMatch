package com.gs.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScoreUtils {

    public static Map<Integer,Integer> perseRank(String rankStr){
        Map<Integer, Integer> rankMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray(rankStr);
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = jsonArray.getJSONObject(i);
                rankMap.put(Integer.parseInt(job.get("rank").toString()),Integer.parseInt(job.get("score").toString()));
            }
        }
        return rankMap;
    }

    public static Integer perseKill(String kill) {
        JSONArray jsonArray = new JSONArray(kill);
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = jsonArray.getJSONObject(i);
                Integer killSocre = Integer.parseInt(job.get("score").toString());
                return killSocre;
            }
        }

        return 0;
    }
}
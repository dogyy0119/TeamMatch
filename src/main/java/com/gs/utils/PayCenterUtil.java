package com.gs.utils;


import cn.hutool.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayCenterUtil {

    public static String PayPersonCost(String memberId, String fee){
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("fee", fee);
        requestMap.put("memberId", memberId);
        requestMap.put("teamId", "0");
        JSONObject json = new JSONObject(requestMap);
        return HttpUtils.doPost("http://127.0.0.1:8083/game/v1.0/paycenter/createCost", json.toString(), null);
    }


    public static String PayTeamCost(String memberId, String teamId, String fee){
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("fee", fee);
        requestMap.put("memberId", memberId);
        requestMap.put("teamId", teamId);
        JSONObject json = new JSONObject(requestMap);
        return HttpUtils.doPost("http://127.0.0.1:8083/game/v1.0/paycenter/createCost", json.toString(), null);
    }


    public static float QueryTeamFunds(String teamId) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("teamId", teamId);
        JSONObject json = new JSONObject(requestMap);
        String result = HttpUtils.doPost("http://127.0.0.1:8083/game/v1.0/check/queryTeamFunds", json.toString(), null);
        JSONObject requestJson = new JSONObject(result);
        if(requestJson.containsKey("fundsFee")) {
            return requestJson.getFloat("fundsFee");
        } else {
            return -1;
        }

    }

    public static float QueryMemberFunds(String memberId) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("memberId", memberId);
        JSONObject json = new JSONObject(requestMap);
        String result = HttpUtils.doPost("http://127.0.0.1:8083/game/v1.0/check/queryMemberFunds", json.toString(), null);
        JSONObject requestJson = new JSONObject(result);
        if(requestJson.containsKey("fundsFee")) {
            return requestJson.getFloat("fundsFee");
        } else {
            return -1;
        }
    }

    public static String QueryTeamAllPayInfo(String teamId) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("teamId", teamId);
        JSONObject json = new JSONObject(requestMap);
        return HttpUtils.doPost("http://127.0.0.1:8083/game/v1.0/check/queryTeamAllPayInfo", json.toString(), null);
    }

    public static String QueryMemberAllPayInfo(String memberId) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("memberId", memberId);
        JSONObject json = new JSONObject(requestMap);
        return HttpUtils.doPost("http://127.0.0.1:8083/game/v1.0/check/queryMemberAllPayInfo", json.toString(), null);
    }
}

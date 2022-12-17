package com.tanhua.test;


import com.alibaba.fastjson.JSON;
import com.tanhua.autoconfig.utils.HttpUtil;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 16:21
 */
public class AipFaceSample {

    public static void main(String[] args) {
        AipFaceSample.faceDetect();
    }


    public static String faceDetect() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
        try {
            Map<String, Object> map = new HashMap<>();

            map.put("image", "https://tanhua12151.oss-cn-hangzhou.aliyuncs.com/demo/11.jpg");
            map.put("face_field", "faceshape,facetype");
            map.put("image_type", "URL");

            String param = JSON.toJSONString(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


        /**
         * 获取权限token
         * @return 返回示例：
         * {
         * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
         * "expires_in": 2592000
         * }
         */
        public static String getAuth() {
            // 官网获取的 API Key 更新为你注册的
            String clientId = "Ll132GocagCDVXLPbbORoeFW";
            // 官网获取的 Secret Key 更新为你注册的
            String clientSecret = "qaLNmOQS2GgP3Ss4PfUHUxsi2E7MwO8O";
            String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
            String getAccessTokenUrl = authHost
                    // 1. grant_type为固定参数
                    + "grant_type=client_credentials"
                    // 2. 官网获取的 API Key
                    + "&client_id=" + clientId
                    // 3. 官网获取的 Secret Key
                    + "&client_secret=" + clientSecret;

            try {
                URL realUrl = new URL(getAccessTokenUrl);
                // 打开和URL之间的连接
                HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                for (String key : map.keySet()) {
                    System.err.println(key + "--->" + map.get(key));
                }
                // 定义 BufferedReader输入流来读取URL的响应
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = "";
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                /**
                 * 返回结果示例
                 */
                System.err.println("result:" + result);
                JSONObject jsonObject = new JSONObject(result);
                String access_token = jsonObject.getString("access_token");
                return access_token;
            } catch (Exception e) {
                System.err.printf("获取token失败！");
                e.printStackTrace(System.err);
            }
            return null;

        }



}

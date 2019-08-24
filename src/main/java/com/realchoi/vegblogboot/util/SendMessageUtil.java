package com.realchoi.vegblogboot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 发送消息至客户端工具类
 */
public class SendMessageUtil {
    /**
     * 发送 json 消息至客户端
     *
     * @param response 响应
     * @param object   消息体
     * @throws Exception
     */
    public static void sendJsonMessage(HttpServletResponse response, Object object) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        // 消息序列化
        printWriter.print(JSON.toJSONString(object));
        printWriter.close();
        response.flushBuffer();
    }
}

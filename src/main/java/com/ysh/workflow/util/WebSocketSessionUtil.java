package com.ysh.workflow.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

@Slf4j
@Component
public class WebSocketSessionUtil {
    /**
     * 使用json序列化对象，然后发送json字符串到session客户端,
     * @param session 用户session
     * @param object 需要传送的对象
     * @param objectMapper json序列化对象
     */
    @Async
    public void sendObject(@NonNull Session session, Object object, ObjectMapper objectMapper) {
        try {
            String data = objectMapper.writeValueAsString(object);
            session.getBasicRemote().sendText(data);
        } catch (JsonProcessingException e) {
            log.info("json序列化异常：", e);
        } catch (IOException e) {
            log.info("IOException：", e);
        }
    }
}

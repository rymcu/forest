package com.rymcu.forest.openai.service.impl;

import com.rymcu.forest.openai.service.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2023/5/26 11:38.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.openai.service.impl
 */
@Slf4j
@Service
public class SseServiceImpl implements SseService {

    private static final Map<Long, SseEmitter> sessionMap = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connect(Long idUser) {
        if (existsUser(idUser)) {
            removeUser(idUser);
        }
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onError((err) -> {
            log.error("type: SseSession Error, msg: {} session Id : {}", err.getMessage(), idUser);
            onError(idUser, err);
        });

        sseEmitter.onTimeout(() -> {
            log.info("type: SseSession Timeout, session Id : {}", idUser);
            removeUser(idUser);
        });

        sseEmitter.onCompletion(() -> {
            log.info("type: SseSession Completion, session Id : {}", idUser);
            removeUser(idUser);
        });
        addUser(idUser, sseEmitter);
        return sseEmitter;
    }

    @Override
    public boolean send(Long idUser, String content) {
        if (existsUser(idUser)) {
            try {
                sendMessage(idUser, content);
                return true;
            } catch (IOException exception) {
                log.error("type: SseSession send Error:IOException, msg: {} session Id : {}", exception.getMessage(), idUser);
            }
        } else {
            throw new IllegalArgumentException("User Id " + idUser + " not Found");
        }
        return false;
    }

    @Override
    public void close(Long idUser) {
        log.info("type: SseSession Close, session Id : {}", idUser);
        removeUser(idUser);
    }

    private void addUser(Long idUser, SseEmitter sseEmitter) {
        sessionMap.put(idUser, sseEmitter);
    }

    private void onError(Long sessionKey, Throwable throwable) {
        SseEmitter sseEmitter = sessionMap.get(sessionKey);
        if (sseEmitter != null) {
            sseEmitter.completeWithError(throwable);
        }
    }

    private void removeUser(Long idUser) {
        sessionMap.remove(idUser);
    }

    private boolean existsUser(Long idUser) {
        return sessionMap.containsKey(idUser);
    }

    private void sendMessage(Long idUser, String content) throws IOException {
        sessionMap.get(idUser).send(content);
    }
}

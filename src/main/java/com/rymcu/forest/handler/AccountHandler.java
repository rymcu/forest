package com.rymcu.forest.handler;

import com.rymcu.forest.handler.event.AccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created on 2022/8/24 14:44.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.handler
 */
@Slf4j
@Component
public class AccountHandler {

    @Async
    @EventListener
    public void processAccountLastLoginEvent(AccountEvent accountEvent) {

    }

}

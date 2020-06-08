package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.entity.SpecialDay;
import com.rymcu.vertical.mapper.SpecialDayMapper;
import com.rymcu.vertical.service.SpecialDayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ronger
 */
@Service
public class SpecialDayServiceImpl extends AbstractService<SpecialDay> implements SpecialDayService {

    @Resource
    private SpecialDayMapper specialDayMapper;

}

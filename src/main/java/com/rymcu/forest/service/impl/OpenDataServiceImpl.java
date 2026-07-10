/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.service.impl;

import com.rymcu.forest.dto.admin.Dashboard;
import com.rymcu.forest.dto.admin.DashboardData;
import com.rymcu.forest.mapper.DashboardMapper;
import com.rymcu.forest.service.OpenDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2022/3/14 13:51.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.service.impl
 */
@Service
public class OpenDataServiceImpl implements OpenDataService {

    @Resource
    private DashboardMapper dashboardMapper;

    @Override
    public Map lastThirtyDaysData() {
        Map map = new HashMap(5);
        ArrayList<String> dates = new ArrayList(30);
        ArrayList<Integer> visitData = new ArrayList(30);
        ArrayList<Integer> visitIpData = new ArrayList(30);
        List<DashboardData> visits = dashboardMapper.selectLastThirtyDaysVisitData();
        List<DashboardData> visitIps = dashboardMapper.selectLastThirtyDaysVisitIpData();
        LocalDate now = LocalDate.now().plusDays(1);
        LocalDate localDate = LocalDate.now().plusDays(-29);
        while (now.isAfter(localDate)) {
            String date = localDate.toString();
            dates.add(date);

            visits.forEach(visit -> {
                if (date.equals(visit.getLabel())) {
                    visitData.add(visit.getValue());
                    return;
                }
            });
            if (visitData.size() < dates.size()) {
                visitData.add(0);
            }

            visitIps.forEach(visitIp -> {
                if (date.equals(visitIp.getLabel())) {
                    visitIpData.add(visitIp.getValue());
                    return;
                }
            });
            if (visitIpData.size() < dates.size()) {
                visitIpData.add(0);
            }

            localDate = localDate.plusDays(1);
        }
        map.put("dates", dates);
        map.put("visits", visitData);
        map.put("visitIps", visitIpData);
        return map;
    }

    @Override
    public Dashboard dashboard() {
        Dashboard dashboard = new Dashboard();
        dashboard.setCountUserNum(dashboardMapper.selectUserCount());
        dashboard.setCountArticleNum(dashboardMapper.selectArticleCount());
        return dashboard;
    }
}

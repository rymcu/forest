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

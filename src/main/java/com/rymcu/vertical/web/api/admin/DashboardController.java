package com.rymcu.vertical.web.api.admin;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.admin.Dashboard;
import com.rymcu.vertical.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping
    public GlobalResult dashboard(){
        Dashboard dashboard = dashboardService.dashboard();
        return GlobalResultGenerator.genSuccessResult(dashboard);
    }
}

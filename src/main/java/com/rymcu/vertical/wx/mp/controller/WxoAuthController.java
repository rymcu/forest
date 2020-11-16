package com.rymcu.vertical.wx.mp.controller;

import com.rymcu.vertical.service.WxUserService;
import com.rymcu.vertical.util.ContextHolderUtils;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ronger
 */
@Controller
@RequestMapping("/wx/oauth/{appId}")
public class WxoAuthController {

    @Resource
    private WxMpService wxMpService;
    @Resource
    private WxUserService wxUserService;

    @Value("${resource.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @GetMapping
    public String wxAuth(@PathVariable String appId, @RequestParam(name = "redirectUrl") String redirectUrl) {
        StringBuilder baseUrl;
        wxMpService.switchoverTo(appId);
        // 测试号
        if ("wxa49093339a5a822b".equals(appId)) {
            baseUrl = new StringBuilder("http://1wx.rymcu.com").append(contextPath);
        } else {
            baseUrl = new StringBuilder(domain).append(contextPath);
        }
        StringBuilder accessTokenUrl = baseUrl.append("/wx/oauth/" + appId + "/getAccessToken?redirectUrl=").append(URIUtil.encodeURIComponent(redirectUrl));
        String oauth2Url = wxMpService.getOAuth2Service().buildAuthorizationUrl(accessTokenUrl.toString(), WxConsts.OAuth2Scope.SNSAPI_BASE, null);

        return "redirect:" + oauth2Url;
    }

    @GetMapping("getAccessToken")
    public String getAccessToken(@PathVariable String appId, @RequestParam(name = "code") String code, @RequestParam(name = "redirectUrl") String redirectUrl) throws Exception {
        wxMpService.switchoverTo(appId);
        WxMpOAuth2AccessToken oAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        boolean valid = wxMpService.getOAuth2Service().validateAccessToken(oAuth2AccessToken);
        if (!valid) {
            throw new Exception("无权限");
        }

        WxMpUser wxMpUser =wxMpService.getUserService().userInfo(oAuth2AccessToken.getOpenId());
        wxUserService.saveUser(wxMpUser,appId);
        ContextHolderUtils.getSession2().setAttribute("wxUser", wxMpUser);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("validJs")
    @ResponseBody
    public WxJsapiSignature validJs(String url) throws WxErrorException {
        return wxMpService.createJsapiSignature(url);
    }

    @GetMapping("t")
    public String reOauth() {
        return "wx/oauth";
    }
}

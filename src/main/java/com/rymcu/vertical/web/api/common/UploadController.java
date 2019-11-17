package com.rymcu.vertical.web.api.common;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.jwt.def.JwtConstants;
import com.rymcu.vertical.web.api.exception.ErrorCode;
import com.rymcu.vertical.web.api.exception.MallApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    @GetMapping("/token")
    public GlobalResult uploadToken(HttpServletRequest request, @RequestParam("q") String q) throws MallApiException {
        /*String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
        if(StringUtils.isBlank(authHeader)){
            throw new MallApiException(ErrorCode.UNAUTHORIZED);
        }*/
        Map map = new HashMap();
        map.put("uploadToken","authHeader.hashCode()");
        map.put("uploadURL","authHeader.hashCode()");
        return GlobalResultGenerator.genSuccessResult(map);
    }
}

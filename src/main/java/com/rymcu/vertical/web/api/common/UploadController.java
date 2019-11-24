package com.rymcu.vertical.web.api.common;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.TUser;
import com.rymcu.vertical.jwt.def.JwtConstants;
import com.rymcu.vertical.util.FileUtils;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.util.Utils;
import com.rymcu.vertical.web.api.exception.ErrorCode;
import com.rymcu.vertical.web.api.exception.MallApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final static String UPLOAD_URL = "/api/upload/file/batch";
    public static final String ctxHeadPicPath = "/usr/local/src/tomcat-hp/webapps/vertical";

    @PostMapping("/file")
    public GlobalResult uploadPicture(@RequestParam(value = "file", required = false) MultipartFile multipartFile, Integer type, HttpServletRequest request){
        if (multipartFile == null) {
            return GlobalResultGenerator.genErrorResult("请选择要上传的文件");
        }
        String typePath = "";
        switch (type){
            case 0:
                typePath = "avatar";
                break;
            case 1:
                typePath = "article";
                break;
            case 2:
                typePath = "tags";
                break;
        }
        //图片存储路径
        String dir = ctxHeadPicPath+"/"+typePath;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String localPath = Utils.getProperty("resource.file-path")+"/"+typePath+"/";
        Map succMap = new HashMap(10);
        Set errFiles = new HashSet();

        String orgName = multipartFile.getOriginalFilename();
        String fileName = System.currentTimeMillis()+"."+ FileUtils.getExtend(orgName).toLowerCase();

        String savePath = file.getPath() + File.separator + fileName;

        File saveFile = new File(savePath);
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
            succMap.put(orgName,localPath+fileName);
        } catch (IOException e) {
            errFiles.add(orgName);
        }
        Map data = new HashMap(2);
        data.put("errFiles",errFiles);
        data.put("succMap",succMap);
        return GlobalResultGenerator.genSuccessResult(data);

    }

    @PostMapping("/file/batch")
    public GlobalResult batchFileUpload(@RequestParam(value = "file[]", required = false)MultipartFile[] multipartFiles,@RequestParam(defaultValue = "1")Integer type,HttpServletRequest request){
        String typePath = "";
        switch (type){
            case 0:
                typePath = "avatar";
                break;
            case 1:
                typePath = "article";
                break;
            case 2:
                typePath = "tags";
                break;
        }
        //图片存储路径
        String dir = ctxHeadPicPath+"/"+typePath;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String localPath = Utils.getProperty("resource.file-path")+"/"+typePath+"/";
        Map succMap = new HashMap(10);
        Set errFiles = new HashSet();

        for(int i=0,len=multipartFiles.length;i<len;i++){
            MultipartFile multipartFile = multipartFiles[i];
            String orgName = multipartFile.getOriginalFilename();
            String fileName = System.currentTimeMillis()+"."+FileUtils.getExtend(orgName).toLowerCase();

            String savePath = file.getPath() + File.separator + fileName;

            File saveFile = new File(savePath);
            try {
                FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
                succMap.put(orgName,localPath+fileName);
            } catch (IOException e) {
                errFiles.add(orgName);
            }
        }
        Map data = new HashMap(2);
        data.put("errFiles",errFiles);
        data.put("succMap",succMap);
        return GlobalResultGenerator.genSuccessResult(data);
    }

    @GetMapping("/token")
    public GlobalResult uploadToken(HttpServletRequest request) throws MallApiException {
        String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
        if(StringUtils.isBlank(authHeader)){
            throw new MallApiException(ErrorCode.UNAUTHORIZED);
        }
        TUser tUser = UserUtils.getTUser(authHeader);
        Map map = new HashMap(2);
        map.put("uploadToken",tUser.getToken());
        map.put("uploadURL", UPLOAD_URL);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}

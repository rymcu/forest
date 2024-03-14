package com.rymcu.forest.web.api.common;

import com.alibaba.fastjson2.JSONObject;
import com.rymcu.forest.auth.JwtConstants;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.LinkToImageUrlDTO;
import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.enumerate.FilePath;
import com.rymcu.forest.service.ForestFileService;
import com.rymcu.forest.util.FileUtils;
import com.rymcu.forest.util.SpringContextHolder;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.util.Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.rymcu.forest.util.SSRFUtil;

/**
 * 文件上传控制器
 *
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final static String UPLOAD_SIMPLE_URL = "/api/upload/file";
    private final static String UPLOAD_URL = "/api/upload/file/batch";
    private final static String LINK_TO_IMAGE_URL = "/api/upload/file/link";
    private static final Environment env = SpringContextHolder.getBean(Environment.class);
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Resource
    private ForestFileService forestFileService;

    public static String uploadBase64File(String fileStr, FilePath filePath) {
        if (StringUtils.isBlank(fileStr)) {
            return "";
        }
        String typePath = filePath.name().toLowerCase();
        //图片存储路径
        String ctxHeadPicPath = env.getProperty("resource.pic-path");
        String dir = ctxHeadPicPath + "/" + typePath;
        File file = new File(dir);
        if (!file.exists()) {
            // 创建文件根目录
            file.mkdirs();
        }

        String localPath = Utils.getProperty("resource.file-path") + "/" + typePath + "/";
        String fileName = System.currentTimeMillis() + ".png";
        String savePath = file.getPath() + File.separator + fileName;
        File saveFile = new File(savePath);
        try {
            FileCopyUtils.copy(Base64.decodeBase64(fileStr.substring(fileStr.indexOf(",") + 1)), saveFile);
            fileStr = localPath + fileName;
        } catch (IOException e) {
            fileStr = "上传失败!";
        }
        return fileStr;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream 输入流
     * @return byte[]
     * @throws IOException IO 异常
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    @PostMapping("/file")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResult<JSONObject> uploadPicture(@RequestParam(value = "file", required = false) MultipartFile multipartFile, @RequestParam(defaultValue = "1") Integer type, HttpServletRequest request) throws IOException {
        if (multipartFile == null) {
            return GlobalResultGenerator.genErrorResult("请选择要上传的文件");
        }
        TokenUser tokenUser = getTokenUser(request);
        JSONObject data = new JSONObject(2);

        if (multipartFile.getSize() == 0) {
            data.put("message", "上传失败!");
            return GlobalResultGenerator.genSuccessResult(data);
        }
        String md5 = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
        String orgName = multipartFile.getOriginalFilename();
        String fileType = FileUtils.getExtend(orgName);
        String fileUrl = forestFileService.getFileUrlByMd5(md5, tokenUser.getIdUser(), fileType);
        if (StringUtils.isNotEmpty(fileUrl)) {
            data.put("url", fileUrl);
            return GlobalResultGenerator.genSuccessResult(data);
        }
        File file = genFile(type);
        String typePath = FilePath.getPath(type);
        String localPath = Utils.getProperty("resource.file-path") + "/" + typePath + "/";
        String fileName = System.currentTimeMillis() + fileType;
        String savePath = file.getPath() + File.separator + fileName;
        fileUrl = localPath + fileName;
        File saveFile = new File(savePath);
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
            forestFileService.insertForestFile(fileUrl, savePath, md5, tokenUser.getIdUser(), multipartFile.getSize(), fileType);
            data.put("url", fileUrl);
        } catch (IOException e) {
            data.put("message", "上传失败!");
        }
        return GlobalResultGenerator.genSuccessResult(data);

    }

    private File genFile(Integer type) {
        String typePath = FilePath.getPath(type);
        //图片存储路径
        String ctxHeadPicPath = env.getProperty("resource.pic-path");
        String dir = ctxHeadPicPath + "/" + typePath;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }
        return file;
    }

    @PostMapping("/file/batch")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResult<JSONObject> batchFileUpload(@RequestParam(value = "file[]", required = false) MultipartFile[] multipartFiles, @RequestParam(defaultValue = "1") Integer type, HttpServletRequest request) {
        TokenUser tokenUser = getTokenUser(request);
        File file = genFile(type);
        String typePath = FilePath.getPath(type);
        String localPath = Utils.getProperty("resource.file-path") + "/" + typePath + "/";
        Map<String, String> successMap = new HashMap<>(16);
        Set<String> errFiles = new HashSet<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String orgName = multipartFile.getOriginalFilename();

            if (multipartFile.getSize() == 0) {
                errFiles.add(orgName);
                continue;
            }
            String fileType = FileUtils.getExtend(orgName);
            try {
                String md5 = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
                String fileUrl = forestFileService.getFileUrlByMd5(md5, tokenUser.getIdUser(), fileType);
                if (StringUtils.isNotEmpty(fileUrl)) {
                    successMap.put(orgName, fileUrl);
                    continue;
                }
                String fileName = System.currentTimeMillis() + fileType;
                String savePath = file.getPath() + File.separator + fileName;
                File saveFile = new File(savePath);
                fileUrl = localPath + fileName;
                FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
                forestFileService.insertForestFile(fileUrl, savePath, md5, tokenUser.getIdUser(), multipartFile.getSize(), fileType);
                successMap.put(orgName, localPath + fileName);
            } catch (IOException e) {
                errFiles.add(orgName);
            }


        }
        JSONObject data = new JSONObject(2);
        data.put("errFiles", errFiles);
        data.put("succMap", successMap);
        return GlobalResultGenerator.genSuccessResult(data);
    }

    private TokenUser getTokenUser(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtConstants.UPLOAD_TOKEN);
        if (StringUtils.isBlank(authHeader)) {
            throw new UnauthorizedException();
        }
        return UserUtils.getTokenUser(authHeader);
    }

    @GetMapping("/simple/token")
    public GlobalResult<JSONObject> uploadSimpleToken(HttpServletRequest request) {
        return getUploadToken(request, UPLOAD_SIMPLE_URL);
    }

    @GetMapping("/token")
    public GlobalResult<JSONObject> uploadToken(HttpServletRequest request) {
        return getUploadToken(request, UPLOAD_URL);
    }

    private GlobalResult<JSONObject> getUploadToken(HttpServletRequest request, String uploadUrl) {
        String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader)) {
            throw new UnauthorizedException();
        }
        TokenUser tokenUser = UserUtils.getTokenUser(authHeader);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uploadToken", tokenUser.getToken());
        jsonObject.put("uploadURL", uploadUrl);
        jsonObject.put("linkToImageURL", LINK_TO_IMAGE_URL);
        return GlobalResultGenerator.genSuccessResult(jsonObject);
    }

    @PostMapping("/file/link")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResult<Map<String, String>> linkToImageUrl(@RequestBody LinkToImageUrlDTO linkToImageUrlDTO, HttpServletRequest request) throws IOException {
        TokenUser tokenUser = getTokenUser(request);
        String url = linkToImageUrlDTO.getUrl();
        Map<String, String> data = new HashMap<>(2);
        if (StringUtils.isBlank(url)) {
            data.put("message", "文件为空!");
            return GlobalResultGenerator.genSuccessResult(data);
        }
        if (url.contains(Utils.getProperty("resource.file-path"))) {
            data.put("originalURL", url);
            data.put("url", url);
            return GlobalResultGenerator.genSuccessResult(data);
        }
        URL link = new URL(url);
        // SSRF 校验
        if (!SSRFUtil.checkUrl(link, false)) {
            throw new FileNotFoundException();
        }
        HttpURLConnection conn = (HttpURLConnection) link.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");
        conn.setRequestProperty("referer", "");

        //得到输入流
        try (InputStream inputStream = conn.getInputStream()) {
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            if (getData.length == 0) {
                data.put("message", "文件为空!");
                return GlobalResultGenerator.genSuccessResult(data);
            }
            // 获取文件md5值
            String md5 = DigestUtils.md5DigestAsHex(getData);
            String fileType = "." + MimeTypeUtils.parseMimeType(conn.getContentType()).getSubtype();
            String fileUrl = forestFileService.getFileUrlByMd5(md5, tokenUser.getIdUser(), fileType);

            data.put("originalURL", url);

            if (StringUtils.isNotEmpty(fileUrl)) {
                data.put("url", fileUrl);
                return GlobalResultGenerator.genSuccessResult(data);
            }
            Integer type = linkToImageUrlDTO.getType();
            if (Objects.isNull(type)) {
                type = 1;
            }
            File file = genFile(type);
            String typePath = FilePath.getPath(type);
            String fileName = System.currentTimeMillis() + fileType;
            fileUrl = Utils.getProperty("resource.file-path") + "/" + typePath + "/" + fileName;

            String savePath = file.getPath() + File.separator + fileName;
            File saveFile = new File(savePath);

            FileCopyUtils.copy(getData, saveFile);
            forestFileService.insertForestFile(fileUrl, savePath, md5, tokenUser.getIdUser(), getData.length, fileType);
            data.put("originalURL", url);
            data.put("url", fileUrl);
            return GlobalResultGenerator.genSuccessResult(data);
        } catch (IOException e) {
            // 上传失败返回原链接
            logger.error("link: {},\nmessage: {}", url, e.getMessage());
            data.put("originalURL", url);
            data.put("url", url);
            return GlobalResultGenerator.genSuccessResult(data);
        }
    }


}

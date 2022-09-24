package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.entity.ForestFile;
import com.rymcu.forest.mapper.ForestFileMapper;
import com.rymcu.forest.service.ForestFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author caterpillar
 * @date 2022-1-12 22:34:55
 */
@Service
public class ForestFileServiceImpl extends AbstractService<ForestFile> implements ForestFileService {

    @Resource
    private ForestFileMapper forestFileMapper;


    /**
     * 通过md5获取文件访问链接
     *
     * @param md5Value  md5值
     * @param createdBy 用户id
     * @param fileType  文件类型
     * @return
     */
    @Override
    public String getFileUrlByMd5(String md5Value, long createdBy, String fileType) {
        return forestFileMapper.getFileUrlByMd5(md5Value, createdBy, fileType);
    }

    /**
     * 插入文件对象
     *
     * @param fileUrl   访问路径
     * @param filePath  上传路径
     * @param md5Value  md5值
     * @param createdBy 创建人
     * @param fileSize  文件大小
     * @param fileType  文件类型
     * @return
     */
    @Override
    public int insertForestFile(String fileUrl, String filePath, String md5Value, long createdBy, long fileSize, String fileType) {
        return forestFileMapper.insertForestFile(fileUrl, filePath, md5Value, createdBy, fileSize, fileType);
    }
}

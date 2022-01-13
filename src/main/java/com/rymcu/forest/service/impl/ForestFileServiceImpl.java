package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.entity.ForestFile;
import com.rymcu.forest.mapper.ForestFileMapper;
import com.rymcu.forest.service.ForestFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author caterpillar
 * @date 2022-1-12 22:34:55
 */
@Service
@Slf4j
public class ForestFileServiceImpl extends AbstractService<ForestFile> implements ForestFileService {

    @Resource
    private ForestFileMapper forestFileMapper;


    /**
     * 通过md5获取文件对象
     *
     * @param md5Value md5值
     * @return
     */
    @Override
    public ForestFile getForestFileByMd5(String md5Value) {
        return forestFileMapper.getForestFileByMd5(md5Value);
    }

    /**
     * 插入文件对象
     *
     * @param fileUrl   访问路径
     * @param filePath  上传路径
     * @param md5Value  md5值
     * @param createdBy 创建人
     * @return
     */
    @Override
    public int insert(String fileUrl, String filePath, String md5Value, long createdBy) {
        return forestFileMapper.insert(fileUrl, filePath, md5Value, createdBy);
    }
}

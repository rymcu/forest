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
    public ForestFile getByMd5(String md5Value) {
        return forestFileMapper.getByMd5(md5Value);
    }

    /**
     * 插入文件对象
     *
     * @param webPath    访问路径
     * @param uploadPath 上传路径
     * @param md5Value   md5值
     * @return
     */
    @Override
    public int insert(String webPath, String uploadPath, String md5Value) {
        return forestFileMapper.insert(webPath, uploadPath, md5Value);
    }
}

package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.entity.ForestFile;
import org.apache.ibatis.annotations.Param;


/**
 * 文件服务记录
 *
 * @author caterpillar
 * @date 2022-1-12 22:32:49
 */
public interface ForestFileService extends Service<ForestFile> {

    /**
     * 通过md5获取文件对象
     *
     * @param md5Value md5值
     * @return
     */
    ForestFile getByMd5(@Param("md5Value") String md5Value);

    /**
     * 插入文件对象
     *
     * @param webPath    访问路径
     * @param uploadPath 上传路径
     * @param md5Value   md5值
     * @return
     */
    int insert(@Param("webPath") String webPath, @Param("uploadPath") String uploadPath, @Param("md5Value") String md5Value);
}

package com.rymcu.forest.lucene.mapper;

import com.rymcu.forest.lucene.model.Baike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BaikeMapper
 *
 * @author suwen
 * @date 2021/2/2 14:10
 */
@Mapper
public interface BaikeMapper {
  List<Baike> getAllBaike(@Param("limit") int limit, @Param("offset") int offset);
}

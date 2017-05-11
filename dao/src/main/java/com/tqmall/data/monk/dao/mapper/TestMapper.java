package com.tqmall.data.monk.dao.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 严禁上传，本地使用
 */

@Component
public interface TestMapper {
    List<Map<String, Object>> selectListBySql(@Param("sql")String sql);
}
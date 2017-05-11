package com.tqmall.data.monk.dao.mapper;


import com.tqmall.data.monk.bean.entity.ImUserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ImUserDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ImUserDO record);

    ImUserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImUserDO record);

    /*=======自定义=======*/
    ImUserDO selectBySys(@Param(value = "sysId")Integer sysId,@Param(value = "sysName")String sysName);
    ImUserDO selectByUid(@Param(value = "uid")String aliUid);
}
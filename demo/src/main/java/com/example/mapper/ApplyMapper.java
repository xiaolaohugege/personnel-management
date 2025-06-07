package com.example.mapper;


import com.example.entity.Apply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplyMapper {

    List<Apply> getListApply(Apply apply);

    int update(Apply apply);

    Apply selectById(Integer id);

    int addApply(Apply apply);

    int deleteApplyById(int id);

}

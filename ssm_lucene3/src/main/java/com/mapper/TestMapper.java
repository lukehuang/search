package com.mapper;

import com.model.Test;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TestMapper {

    int insert(Test record);

    int insertSelective(Test record);

    int updateByPrimaryKey(Test record);

    int updateByPrimaryKeySelective(Test record);

    int deleteByPrimaryKey(Integer id);

    Test selectByPrimaryKey(Integer id);

    /************************************************************分割线************************************************************/
    /* todo 搜索 */

    /* 搜索条件收集（搜索自身字段所有不同种类的值） */
    List<Test> selectPropertyGroup(@Param("property") String property);

    /* 多重模糊搜索（AND） */
    List<Test> selectFuzzy(Map select);

    /* 多重模糊+条件搜索 */
    List<Test> selectFuzzy2(Map select);

}

package com.enterprise.mapper.suites;

import com.enterprise.base.entity.SuitesEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description SuitesMapper
 * @Author shisan
 * @Date 2018/3/26 上午10:02
 */
public interface SuitesMapper {

    /**
     * 根据条件查询 SuitesEntity
     *
     * @param suites 查询条件
     * @author shisan
     * @date 2017/11/30 下午2:18
     */
    SuitesEntity getSuitesentity(SuitesEntity suites);

    /**
     * 根据条件修改 SuitesEntity
     *
     * @param suites 修改条件
     * @author shisan
     * @date 2017/11/30 下午2:42
     */
    void modifySuites(SuitesEntity suites);

    /**
     * 获取需要刷新的套件
     *
     * @author shisan
     * @date 2018/2/5 下午6:11
     */
    List<SuitesEntity> seachRefeshSuites();

    SuitesEntity getSuiteByKey(@Param("suiteKey")String suitKey);
}
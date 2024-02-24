package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.model.response.SysUserBaseInfoBO;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    /**
     * 取用户实体，忽略行级租户拦截器
     * @param pin 账号
     * @return SysUserEntity
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUserEntity getUserByPin(@Param(value = "pin") String pin);

    /**
     * 取用户基本信息，忽略行级租户拦截器
     * @param userId 用户ID
     * @return SysUserBaseInfoBO
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUserBaseInfoBO getBaseInfoByUserId(@Param(value = "userId") Long userId);

    /**
     * 查询所有用户IDs
     * @param statusEnums 仅保留符合指定状态的，可以为null
     */
    default List<Long> selectIds(Collection<EnabledStatusEnum> statusEnums) {
        return selectList(
                new LambdaQueryWrapper<SysUserEntity>()
                        // 只取主键ID
                        .select(SysUserEntity::getId)
                        // 状态
                        .in(CollUtil.isNotEmpty(statusEnums), SysUserEntity::getStatus, statusEnums)
        ).stream().map(SysUserEntity::getId).collect(Collectors.toList());
    }

}

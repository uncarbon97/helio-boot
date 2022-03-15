package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.model.response.SysUserBaseInfoBO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Param;

/**
 * 后台用户
 *
 * @author Uncarbon
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    @InterceptorIgnore(tenantLine = "true")
    SysUserEntity getUserByPin(@Param(value = "pin") String pin);

    @InterceptorIgnore(tenantLine = "true")
    void updateLastLoginAt(@Param(value = "userId") Long userId, @Param(value = "lastLoginAt") LocalDateTime lastLoginAt);

    @InterceptorIgnore(tenantLine = "true")
    SysUserBaseInfoBO getBaseInfoByUserId(@Param(value = "userId")Long userId);

}

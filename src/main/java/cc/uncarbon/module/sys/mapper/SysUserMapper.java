package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.model.response.SysUserBaseInfoBO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * 后台用户
 *
 * @author Uncarbon
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    @InterceptorIgnore(tenantLine = "true")
    void updateLastLoginAt(Long userId, LocalDateTime lastLoginAt);

    @InterceptorIgnore(tenantLine = "true")
    SysUserBaseInfoBO getBaseInfoByUserId(Long userId);

}

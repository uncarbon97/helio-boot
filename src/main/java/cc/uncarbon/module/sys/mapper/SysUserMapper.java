package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.model.response.SysUserBaseInfoBO;
import cc.uncarbon.module.sys.processor.IgnoredTenant;
import cc.uncarbon.module.sys.processor.RequiredIgnoredTenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 后台用户
 *
 * @author Uncarbon
 */
@Mapper
// 本来下的所有方法，都无视行级租户拦截器
//@InterceptorIgnore(tenantLine = "true")
@RequiredIgnoredTenant
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    @IgnoredTenant
//    @InterceptorIgnore(tenantLine = "true")
    SysUserEntity getUserByPin(@Param(value = "pin") String pin);

    @IgnoredTenant
    SysUserBaseInfoBO getBaseInfoByUserId(@Param(value = "userId") Long userId);

}

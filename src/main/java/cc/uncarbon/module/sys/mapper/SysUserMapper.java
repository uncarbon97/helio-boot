package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.model.response.SysUserBaseInfoBO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
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
@InterceptorIgnore(tenantLine = "true")
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    SysUserEntity getUserByPin(@Param(value = "pin") String pin);

    SysUserBaseInfoBO getBaseInfoByUserId(@Param(value = "userId") Long userId);

}

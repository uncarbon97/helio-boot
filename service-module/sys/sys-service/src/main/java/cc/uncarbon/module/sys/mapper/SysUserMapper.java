package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.model.response.SysUserBaseInfoBO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

}

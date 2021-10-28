package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysUserRoleRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台用户-角色关联
 * @author Uncarbon
 */
@Mapper
public interface SysUserRoleRelationMapper extends BaseMapper<SysUserRoleRelationEntity> {
	
}

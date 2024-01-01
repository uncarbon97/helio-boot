package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysRoleMenuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台角色-可见菜单关联
 */
@Mapper
public interface SysRoleMenuRelationMapper extends BaseMapper<SysRoleMenuRelationEntity> {
	
}

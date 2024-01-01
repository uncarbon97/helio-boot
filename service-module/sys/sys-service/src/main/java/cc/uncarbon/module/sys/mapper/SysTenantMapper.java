package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysTenantEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统租户
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenantEntity> {
	
}

package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.module.sys.entity.SysLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台操作日志
 * @author Uncarbon
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLogEntity> {
	
}

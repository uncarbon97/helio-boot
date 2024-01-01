package cc.uncarbon.module.oss.mapper;

import cc.uncarbon.framework.crud.mapper.HelioBaseMapper;
import cc.uncarbon.module.oss.entity.OssFileInfoEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 上传文件信息
 */
@Mapper
public interface OssFileInfoMapper extends HelioBaseMapper<OssFileInfoEntity, Long> {

}

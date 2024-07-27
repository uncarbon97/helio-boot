package cc.uncarbon.module.sys.mapper;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.module.sys.entity.SysDataDictClassifiedEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;

import java.util.Objects;


/**
 * 数据字典分类
 */
@Mapper
public interface SysDataDictClassifiedMapper extends BaseMapper<SysDataDictClassifiedEntity> {

    default SysDataDictClassifiedEntity selectByCode(@Nonnull String code, @Nullable EnabledStatusEnum status) {
        return selectOne(
                new QueryWrapper<SysDataDictClassifiedEntity>()
                        .lambda()
                        .eq(SysDataDictClassifiedEntity::getCode, code)
                        .eq(Objects.nonNull(status), SysDataDictClassifiedEntity::getStatus, status)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );
    }

}

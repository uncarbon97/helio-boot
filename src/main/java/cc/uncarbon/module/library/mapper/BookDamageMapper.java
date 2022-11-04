package cc.uncarbon.module.library.mapper;

import cc.uncarbon.module.library.entity.BookDamageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 书籍损坏记录
 *
 * @author Uncarbon
 */
@Mapper
public interface BookDamageMapper extends BaseMapper<BookDamageEntity> {

}

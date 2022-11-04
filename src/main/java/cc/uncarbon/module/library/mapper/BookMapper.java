package cc.uncarbon.module.library.mapper;

import cc.uncarbon.module.library.entity.BookEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 书籍
 *
 * @author Uncarbon
 */
@Mapper
public interface BookMapper extends BaseMapper<BookEntity> {

}

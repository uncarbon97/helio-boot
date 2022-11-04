package cc.uncarbon.module.library.mapper;

import cc.uncarbon.module.library.entity.BookBorrowingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 书籍借阅记录
 *
 * @author Uncarbon
 */
@Mapper
public interface BookBorrowingMapper extends BaseMapper<BookBorrowingEntity> {

}

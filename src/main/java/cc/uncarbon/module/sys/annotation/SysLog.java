package cc.uncarbon.module.sys.annotation;

import java.lang.annotation.*;


/**
 * 放在方法上，可将操作记录至系统日志中
 *
 * @author Uncarbon
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SysLog {

    /**
     * 操作内容(如:新增部门)
     */
    String value() default "";

    /**
     * 是否同步保存至系统日志数据表中
     * true = 同步保存：如果开启了事务/事务注解，若系统日志保存失败，则会抛出异常触发回滚，使得本次操作也失败
     * false = 异步保存：若系统日志保存失败，不影响本次操作
     */
    boolean syncSaving() default false;

}

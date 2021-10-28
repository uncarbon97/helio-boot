package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;


/**
 * 系统租户
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_tenant")
public class SysTenantEntity extends HelioBaseEntity<Long> {

    @ApiModelProperty(value = "租户名")
    @TableField(value = "tenant_name")
    private String tenantName;

    @ApiModelProperty(value = "状态(0=禁用 1=启用)")
    @TableField(value = "status")
    private GenericStatusEnum status;

    @ApiModelProperty(value = "租户管理员用户ID")
    @TableField(value = "tenant_admin_user_id")
    private Long tenantAdminUserId;

}

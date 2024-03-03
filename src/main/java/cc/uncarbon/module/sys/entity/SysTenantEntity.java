package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;


/**
 * 系统租户
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_tenant")
public class SysTenantEntity extends HelioBaseEntity<Long> {

    @Schema(description = "租户名")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "状态")
    @TableField(value = "status")
    private EnabledStatusEnum status;

    @Schema(description = "租户管理员用户ID")
    @TableField(value = "tenant_admin_user_id")
    private Long tenantAdminUserId;

    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;

}

package cc.uncarbon.module.sys.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 系统租户-需强制登出用户 BO
 * 同一时间大量登出，会操作大量Redis键，可能存在缓存雪崩的风险
 */
@Getter
public class SysTenantKickOutUsersBO {

    @ApiModelProperty(value = "后台用户IDs")
    private final List<Long> sysUserIds;

    public SysTenantKickOutUsersBO() {
        this.sysUserIds = Collections.emptyList();
    }

    public SysTenantKickOutUsersBO(List<Long> sysUserIds) {
        this.sysUserIds = sysUserIds;
    }
}

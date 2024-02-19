package cc.uncarbon.module.adminapi.listener;

import cc.uncarbon.module.adminapi.event.KickOutSysUsersEvent;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * admin-api模块事件监听器
 */
@Component
@RequiredArgsConstructor
public class AdminApiEventListener {

    private final ThreadPoolTaskExecutor taskExecutor;


    @EventListener(value = KickOutSysUsersEvent.class)
    public void handleKickOutSysUsersEvent(KickOutSysUsersEvent event) {
        Collection<Long> sysUserIds = event.getData().getSysUserIds();
        if (CollUtil.isNotEmpty(sysUserIds)) {
            // 异步强制登出；同一时间大量登出，会操作大量Redis键，可能存在缓存雪崩的风险
            taskExecutor.submit(() -> sysUserIds.forEach(AdminStpUtil::kickout));
        }
    }
}

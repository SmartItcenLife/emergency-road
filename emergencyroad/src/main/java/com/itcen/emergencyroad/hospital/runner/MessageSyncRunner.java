package com.itcen.emergencyroad.hospital.runner;

import com.itcen.emergencyroad.hospital.service.MessageSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// Message Sync 를 위한 단독 테스트용 코드
@Component
@Profile("local")
@RequiredArgsConstructor
public class MessageSyncRunner implements CommandLineRunner {
    private final MessageSyncService messageSyncService;

    @Override
    public void run(String... args) throws Exception {
        messageSyncService.syncBySido("서울특별시",1,100);
    }
}

package com.tekal.elevatortechtest.service.impl;

import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.service.ElevatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TuViejaElevatorService implements ElevatorService {
    @Override
    public void processElevatorCall(ElevatorCall elevatorCall) {
        try {
            for (int i = 0; i < 5; i++) {
                TimeUnit.SECONDS.sleep(1);
                log.info("  TTTTTTT  U   U     V   V  IIIII  EEEEE  JJJJJ   AAAAA");
                log.info("     T     U   U     V   V    I    E        J    A     A");
                log.info("     T     U   U     V   V    I    EEEE     J    AAAAAAA");
                log.info("     T     U   U      V V     I    E      J J    A     A");
                log.info("     T      UUU        V    IIIII  EEEEE    J    A     A");
                log.info("");
            }
            log.info("TU VIEJA!");
        } catch (InterruptedException e) {
            log.error("Error in TuViejaElevatorService", e);
        }
    }
}

package org.example.socks.storage.service.sock.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.example.socks.storage.model.sock.dto.SockDTO;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SockServiceAspect {
    @Before("execution(public Long incomeSock(org.example.socks.storage.model.sock.dto.SockDTO))")
    public void beforeIncomeSockAdvice(JoinPoint joinPoint) {
        SockDTO sockDTO = (SockDTO) joinPoint.getArgs()[0];
        log.info(String.format("Attempt to save socks - %s", sockDTO));
    }

    @AfterReturning(pointcut = "execution(public Long incomeSock(org.example.socks.storage.model.sock.dto.SockDTO))", returning = "result")
    public void afterIncomeSockAdvice(Long result) {
        log.info(String.format("Socks has been saved. Current count - %s.", result));
    }

    @Before("execution(public Long outcomeSock(org.example.socks.storage.model.sock.dto.SockDTO))")
    public void beforeOutcomeSockAdvice(JoinPoint joinPoint) {
        SockDTO sockDTO = (SockDTO) joinPoint.getArgs()[0];
        log.info(String.format("Attempt to take socks - %s", sockDTO));
    }

    @AfterReturning(pointcut = "execution(public Long outcomeSock(org.example.socks.storage.model.sock.dto.SockDTO))", returning = "result")
    public void afterOutcomeSockAdvice(Long result) {
        log.info(String.format("Socks has been taken. Current count - %s.", result));
    }

    @Before("execution(public Long countByConstraints(String, Integer, Integer, Integer))")
    public void beforeCountByConstraintsAdvice(JoinPoint joinPoint) {
        String color = (String) joinPoint.getArgs()[0];
        Integer lessThan = (Integer) joinPoint.getArgs()[1];
        Integer equal = (Integer) joinPoint.getArgs()[2];
        Integer moreThan = (Integer) joinPoint.getArgs()[3];
        log.info(String.format("Attempt to find socks by constraints: color = %s, lessThan = %s, equal = %s, moreThan = %s.", color, lessThan, equal, moreThan));
    }

    @AfterReturning(pointcut = "execution(public Long countByConstraints(String, Integer, Integer, Integer))", returning = "result")
    public void afterCountByConstraintsAdvice(Long result) {
        log.info(String.format("Current socks count by constraints - %s.", result));
    }

    @Before("execution(public void updateSocks(Long, String, Integer, Long))")
    public void beforeUpdateSocksAdvice(JoinPoint joinPoint) {
        Long id = (Long) joinPoint.getArgs()[0];
        String color = (String) joinPoint.getArgs()[1];
        Integer cottonPercent = (Integer) joinPoint.getArgs()[2];
        Long count = (Long) joinPoint.getArgs()[3];
        log.info(String.format("Attempt to update socks: id = %s, color = %s, cotton = %s, count = %s.", id, color, cottonPercent, count));
    }

    @After("execution(public void updateSocks(Long, String, Integer, Long))")
    public void afterUpdateSocksAdvice(JoinPoint joinPoint) {
        Long id = (Long) joinPoint.getArgs()[0];
        log.info(String.format("Socks with id %s were updated.", id));
    }
}

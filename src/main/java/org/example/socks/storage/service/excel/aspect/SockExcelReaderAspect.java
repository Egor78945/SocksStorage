package org.example.socks.storage.service.excel.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
@Slf4j
public class SockExcelReaderAspect {
    @Before("execution(public void readSheet(org.springframework.web.multipart.MultipartFile))")
    public void beforeReadSheetAdvice(JoinPoint joinPoint){
        MultipartFile multipartFile = (MultipartFile) joinPoint.getArgs()[0];
        log.info(String.format("Attempt to read excel file - %s", multipartFile.getName()));
    }

    @After("execution(public void readSheet(org.springframework.web.multipart.MultipartFile))")
    public void afterReadSheetAdvice(JoinPoint joinPoint){
        MultipartFile multipartFile = (MultipartFile) joinPoint.getArgs()[0];
        log.info(String.format("Excel file has been read - %s", multipartFile.getName()));
    }
}

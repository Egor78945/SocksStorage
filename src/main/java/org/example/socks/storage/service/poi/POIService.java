package org.example.socks.storage.service.poi;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class POIService {
    public XSSFWorkbook getBookFromExcelFile(MultipartFile multipartFile) throws IOException {
        return new XSSFWorkbook(multipartFile.getInputStream());
    }
}

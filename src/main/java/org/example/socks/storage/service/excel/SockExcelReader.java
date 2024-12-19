package org.example.socks.storage.service.excel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.socks.storage.model.sock.dto.SockDTO;
import org.example.socks.storage.service.poi.POIService;
import org.example.socks.storage.service.sock.SockService;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
@Tag(name = "Excel file reader")
public class SockExcelReader extends POIService {
    private final SockService sockService;

    @Operation(description = "Reads an Excel file and saves the data as new socks.")
    public void readSheet(MultipartFile multipartFile) {
        try {
            XSSFWorkbook book = getBookFromExcelFile(multipartFile);
            int i = 0;
            while (i < book.getNumberOfSheets()) {
                XSSFSheet sheet = book.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.rowIterator();
                SockDTO dto = new SockDTO();
                while (rowIterator.hasNext()) {
                    XSSFRow row = (XSSFRow) rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    String color = cellIterator.next().getStringCellValue();
                    int cottonPercent = (int) cellIterator.next().getNumericCellValue();
                    long count = (long) cellIterator.next().getNumericCellValue();

                    dto.setColor(color);
                    dto.setCottonPercent(cottonPercent);
                    dto.setCount(count);

                    try {
                        sockService.incomeSock(dto);
                    } catch (RuntimeException ignored){

                    }
                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

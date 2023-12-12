package com.cyb.codetest.excel处理;

import com.alibaba.fastjson.JSON;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * zip-bomb解决方法
 * @auther cyb
 * @date 2023/11/29 17:08
 */
public class TestZipBomb {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        File file =new File("/Users/chenyunbin/供应链中台线上化/西西12月13日盖亚.xlsx");

//        ZipSecureFile.setMinInflateRatio(0);

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow firstRow = sheet.getRow(1);
        XSSFCell firstCell = firstRow.getCell(0);


    }
}

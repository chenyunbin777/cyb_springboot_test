package com.cyb.codetest.excel处理;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @auther cyb
 * @date 2023/11/29 17:08
 */
public class TestExcelNumber {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        File file =new File("/Users/chenyunbin/供应链中台线上化/直播运营上传模板-新字段测试.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow firstRow = sheet.getRow(1);
        XSSFCell firstCell = firstRow.getCell(0);


        String strValue = Double.toString(firstCell.getNumericCellValue());

        BigDecimal bd1 = new BigDecimal(Double.toString(firstCell.getNumericCellValue()));

        String plainString = bd1.toPlainString();
        System.out.println("strValue:"+strValue);
        if(plainString.contains(".")){
            System.out.println("EEEEEE");
        }else {
            // 去掉后面无用的零  如小数点后面全是零则去掉小数点
            String value = bd1.toPlainString().replaceAll("0+?$", "").replaceAll("[.]$", "");

            System.out.println(JSON.toJSONString(bd1));

            System.out.println(value);

            System.out.println(bd1.toPlainString());
        }



    }
}

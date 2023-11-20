package com.cyb.codetest.excel处理;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Cell中图片展示的信息  DISPIMG("ID_4DEC1821A20149449744B7778CEB2CD0",1)
 * /xl/_rels/cellimages.xml.rels  rid与图片文件地址关系
 * /xl/cellimages.xml rid与图片编号ID关系
 * /xl/worksheets/sheet1.xml 工作表与图片关系（/xl/worksheets/目录下有多个sheet.xml文件，分别代表不同的工作表，sheet1.xml表示是第一个工作表）
 * /xl/media/ 下都是图片信息
 * @auther cyb
 * @date 2023/11/20 14:23
 */
@Slf4j
public class TestExcelParseImage {


    public static void main(String[] args) throws IOException, InvalidFormatException {
        File file =new File("/Users/chenyunbin/供应链中台线上化/图片测试/嵌入式图片测试.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow firstRow = sheet.getRow(0);
        XSSFCell firstCell = firstRow.getCell(0);

        //嵌入式图片ID存储
        //例如：{"4DEC1821A20149449744B7778CEB2CD0":"rId1"}
        Map<String, String> imageIdMappingMap = ridWithIDRelationShip(file);
        //嵌入式图片存储
        //例如：{"rId1":"media/image1.png"}
        Map<String, String> imageMap = ridWithImagePathRelationShip(file);

        System.out.println("imageIdMappingMap:"+ JSON.toJSONString(imageIdMappingMap));
        System.out.println("imageMap:"+ JSON.toJSONString(imageMap));

        //Cell中图片展示的信息：DISPIMG("ID_4DEC1821A20149449744B7778CEB2CD0",1)
        //获取
        String imageId =  firstCell.getStringCellValue().substring(firstCell.getStringCellValue().indexOf("ID_") + 3, firstCell.getStringCellValue().lastIndexOf("\""));
        System.out.println("imageId:"+imageId);

        //获取到rId
        String rId = imageIdMappingMap.get(imageId);

        //获取Excel压缩包中图片的位置，通过rId获取到图片路径，再通过这个path获取到图片文件
        String imagePath = imageMap.get(rId);
        //处理图片逻辑
        String imageUrl = handlePathImage(file, imagePath);


//        zip4jWithNoPassword("/Users/chenyunbin/供应链中台线上化/图片测试/嵌入式图片测试.zip","/Users/chenyunbin/供应链中台线上化/图片测试/压缩");


    }

    public static void zip4jWithNoPassword(String zipFileName, String folderPath ){
        ZipParameters zipParameters = new ZipParameters();
        try {
            new net.lingala.zip4j.ZipFile(zipFileName).addFolder(new File(folderPath),zipParameters);
        } catch (ZipException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析/xl/cellimages.xml rid与图片编号ID关系
     * @param file：excel源文件
     * @return：
     * @throws IOException
     */
    private static Map<String,String> ridWithIDRelationShip(File file) throws IOException {
        Map<String, String> imageIdMappingMap = new HashMap<>();
        InputStream inputStream = null;
        try {

            //读取关系xml文件
            inputStream = openFile(file, "xl/cellimages.xml");
            if(inputStream == null){
                //导入的excel，也有可能没有这个文件（xl/cellimages.xml）
                return imageIdMappingMap;
            }
            // 创建SAXReader对象
            SAXReader reader = new SAXReader();
            // 加载xml文件
            Document dc = reader.read(inputStream);
            // 获取根节点
            Element rootElement = dc.getRootElement();
            //获取子节点 每一个图片节点
            List<Element> cellImageList = rootElement.elements();

            //循环处理每一个图片
            for (Element cellImage : cellImageList) {
                Element pic = cellImage.element("pic");
                Element nvPicPr = pic.element("nvPicPr");
                Element cNvPr = nvPicPr.element("cNvPr");
                //图片id
                String imageId = cNvPr.attribute("name").getValue().replace("ID_", "");
//                        imageId = subImageId(imageId);
                Element blipFill = pic.element("blipFill");
                Element blip = blipFill.element("blip");
                //图片Rid
                String imageRid = blip.attribute("embed").getValue();
                //存入map中
                imageIdMappingMap.put(imageId, imageRid);
            }

        } catch (Exception e) {
            log.error("ridWithIDRelationShip 错误 file:{} :",file.getName(), e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return imageIdMappingMap;
    }

    /**
     *
     * @param file：excel源文件
     * @return
     * @throws IOException
     */
    private static Map<String, String> ridWithImagePathRelationShip(File file) throws IOException {
        Map<String, String> imageMap = new HashMap<>();

        InputStream inputStream = null;
        try {
            //读取关系文件
            inputStream = openFile(file, "xl/_rels/cellimages.xml.rels");
            if(inputStream == null){
                //导入的excel，也有可能没有这个文件（xl/_rels/cellimages.xml.rels）
                return imageMap;
            }
            // 创建SAXReader对象
            SAXReader reader = new SAXReader();
            // 加载xml文件
            Document dc = reader.read(inputStream);
            // 获取根节点
            Element rootElement = dc.getRootElement();

            List<Element> imageRelationshipList = rootElement.elements();

            //处理每个关系
            for (Element imageRelationship : imageRelationshipList) {
                String imageRid = imageRelationship.attribute("Id").getValue();
                String imagePath = imageRelationship.attribute("Target").getValue();
                imageMap.put(imageRid, imagePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return imageMap;
    }

    /**
     * 将excel转化成zip压缩文件，然后根据压缩文件中的文件路径filePath获取文件流
     * @param file：excel源文件
     * @param filePath：压缩文件中的文件路径
     * @return
     */
    private static InputStream openFile(File file, String filePath) {
        try {

            ZipFile zipFile = new ZipFile(file);
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));

            ZipEntry nextEntry = null;
            while ((nextEntry = zipInputStream.getNextEntry()) != null) {
                String name = nextEntry.getName();
                if (name.equalsIgnoreCase(filePath)) {
                    return zipFile.getInputStream(nextEntry);
                }
            }
        } catch (Exception e) {
            log.error("openFile error, file:{},filePath:{}", file.getName(), filePath);
        }
        return null;
    }


    /**
     * excel文件中的图片逻辑
     * @param file：excel源文件
     * @param path：media/image1.png，就是图片在压缩包中的路径
     * @return
     */
    public static String handlePathImage(File file, String path) {
        InputStream inputStream = null;
        log.info("handlePathImage,local图片路径,file:{},path:{}",file.getName(),path);
        try {

            //获取图片inputStream
            inputStream = openFile(file, "xl/" + path);
            String[] fileArr = path.split("\\.");
            //图片文件后缀
            String ext = fileArr[fileArr.length - 1];
            // 文件名
            String fileName = System.currentTimeMillis() + Math.random() * 100 + "." + ext;

            //文件逻辑处理
            //......

        }catch (Exception e){
            log.error("handlePathImage error,file:{},path:{}, e:",file.getName(),path,e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception ignore){}
            }
        }
        return "";
    }
}

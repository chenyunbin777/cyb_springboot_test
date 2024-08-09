package com.cyb.codetest.io;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @auther cyb
 * @date 2024/7/11 14:00
 */
public class 嵌入式图片 {


    public static void main(String[] args) throws FileNotFoundException {
//        "xl/" + media/image2.png

        File importFile = getImportFile("https://public-xincheng-test.oss-cn-hangzhou.aliyuncs.com/supply/admin/images/efc766c3fc054cd996ad7fe849c2d1a0/群组导入.xlsx");

        //media/image1.jpeg
//        InputStream inputStream = openFile(importFile, "xl/media/image2.png");

        InputStream inputStream = openFile(importFile, "xl/media/image1.jpeg");


//        Path path = Path.of("output.txt"); // 指定输出文件路径

        String tempFile = "/Users/chenyunbin/供应链中台线上化/test.png";
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


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
            System.out.println("openFile error");
//            log.error("openFile error, file:{},filePath:{}", file.getName(), filePath);
        }
        return null;
    }

    public static File getImportFile(String fileUrl) {
        Assert.notNull(fileUrl, "fileUrl should not be null!");
        InputStream ins = null;
        try {


            ins = new URL(setInternal(fileUrl)).openStream(); String fileNameWithSuffix = StringUtils.substring(fileUrl, fileUrl.lastIndexOf("."));
            File file = new File(System.getProperty("user.dir") + "/" + System.currentTimeMillis() + toMD5(fileUrl) + fileNameWithSuffix);//System.getProperty("java.io.tmpdir")缓存
            if (file.exists()) {
                file.delete();//如果缓存中存在该文件就删除
            }
            OutputStream os = new FileOutputStream(file);
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
            return file;
        } catch (Exception e) {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ex) {
//                    log.error("batchImport getImportFile close error", ex);
                }
            }
//            log.error("文件本地化异常,fileUrl:{}, e:", fileUrl, e);
//            throw new BizException(5005, "文件本地化异常 fileUrl:" + fileUrl);
        }
        return null;
    }


    public static String setInternal(String fileUrl){
        return fileUrl.replace("oss-cn-hangzhou.aliyuncs.com", "oss-cn-hangzhou-internal.aliyuncs.com");
    }

    public static String toMD5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 反复调用update输入数据:
            md.update(str.getBytes("UTF-8"));
            // 调用digest()获取HelloWorld的MD5计算结果
            byte[] result = md.digest();
            return new BigInteger(1, result).toString(16);
        }catch (Exception ignore){

        }
        return str;
    }
}

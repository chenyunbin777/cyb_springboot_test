package com.cyb.codetest.设计模式.行为型模式.责任链模式.日志处理级别链;

/**
 * @author cyb
 * @date 2022/9/10 下午3:30
 */
public class FileLogger extends AbstractLogger {

    public FileLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("File::Logger: " + message);
    }
}
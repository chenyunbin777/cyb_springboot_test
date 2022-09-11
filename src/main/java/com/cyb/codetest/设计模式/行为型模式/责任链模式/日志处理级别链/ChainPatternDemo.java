package com.cyb.codetest.设计模式.行为型模式.责任链模式.日志处理级别链;

/**
 * @author cyb
 * @date 2022/9/10 下午3:30
 */
public class ChainPatternDemo {

    private static AbstractLogger getChainOfLoggers(){

        AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.DEBUG);
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);

        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(consoleLogger);

        return errorLogger;
    }

    public static void main(String[] args) {
        AbstractLogger loggerChain = getChainOfLoggers();

        System.out.println(loggerChain.level);

        loggerChain.logMessage(AbstractLogger.INFO, "This is an information.");

//        loggerChain.logMessage(AbstractLogger.DEBUG, "This is a debug level information.");
//        loggerChain.logMessage(AbstractLogger.ERROR, "This is an error information.");
    }
}

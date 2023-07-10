package com.tr.chat.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import sun.reflect.Reflection;

import java.util.Date;

@Component
public class LoggerUtil {
    private static boolean isLog = true;
    private static Logger logger = null;

    public static void setLogger(boolean isLog) {
        LoggerUtil.isLog = isLog;
    }

    public static void setLog(Logger logger) {
        LoggerUtil.logger = logger;
    }

    public static void setLogger(Logger logger) {
        LoggerUtil.logger = logger;
    }

    public static void debug(Object msg) {
        if (isLog) {
            if (logger == null) {
                StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
                if (stackTraceElement.length > 3) {
                    logger = LogManager.getLogger(stackTraceElement[2].getClassName());
                    logger.debug("调用者类名" + stackTraceElement[2].getClassName());
                } else {
                    logger = LogManager.getLogger(Reflection.getCallerClass(2).getName());
                    logger.debug("调用者类名" + Reflection.getCallerClass(2).getName());
                }
            }
            logger.debug(new Date() + " " + msg);
        }

    }

    public static void info(Object msg) {
        if (isLog) {
            if (logger == null) {
                StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
                if (stackTraceElement.length > 3) {
                    logger = LogManager.getLogger(stackTraceElement[2].getClassName());
                    logger.debug("调用者类名" + stackTraceElement[2].getClassName());
                } else {
                    logger = LogManager.getLogger(Reflection.getCallerClass(2).getName());
                    logger.debug("调用者类名" + Reflection.getCallerClass(2).getName());
                }
            }
            logger.info(new Date() + " " + msg);
        }
    }

    public static void warn(Object msg) {
        if (isLog) {
            if (logger == null) {
                StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
                if (stackTraceElement.length > 3) {
                    logger = LogManager.getLogger(stackTraceElement[2].getClassName());
                    logger.debug("调用者类名" + stackTraceElement[2].getClassName());
                } else {
                    logger = LogManager.getLogger(Reflection.getCallerClass(2).getName());
                    logger.debug("调用者类名" + Reflection.getCallerClass(2).getName());
                }
            }
            logger.warn(new Date() + " " + msg);
        }
    }

    public static void error(Object msg) {
        if (isLog) {
            if (logger == null) {
                StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
                if (stackTraceElement.length > 3) {
                    logger = LogManager.getLogger(stackTraceElement[2].getClassName());
                    logger.debug("调用者类名" + stackTraceElement[2].getClassName());
                } else {
                    logger = LogManager.getLogger(Reflection.getCallerClass(2).getName());
                    logger.debug("调用者类名" + Reflection.getCallerClass(2).getName());
                }
            }
            logger.error(new Date() + " " + msg);
        }
    }

    public static void fatal(Object msg) {
        if (isLog) {
            if (logger == null) {
                StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
                if (stackTraceElement.length > 3) {
                    logger = LogManager.getLogger(stackTraceElement[2].getClassName());
                    logger.debug("调用者类名" + stackTraceElement[2].getClassName());
                } else {
                    logger = LogManager.getLogger(Reflection.getCallerClass(2).getName());
                    logger.debug("调用者类名" + Reflection.getCallerClass(2).getName());
                }
            }
            logger.fatal(new Date() + " " + msg);
        }
    }

}
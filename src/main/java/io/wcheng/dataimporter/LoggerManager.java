package io.wcheng.dataimporter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

public class LoggerManager {

    private Logger defaultLogger;
    private Logger logger;

    public LoggerManager() {
        defaultLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);
    }

    public void initLogger(String filePath, String loggerLevel, String handlerLevel) throws IOException {
        logger = Logger.getLogger("DataImporter");
        try {
            logger.setLevel(Level.parse(loggerLevel));
        } catch (IllegalArgumentException e) {
            defaultLogger.log(Level.WARNING, "loggerLevel is invalid", e);
        }
        Handler handler = new FileHandler(filePath, true);
        handler.setEncoding(StandardCharsets.UTF_8.name());
        handler.setFormatter(new SimpleFormatter());
        try {
            handler.setLevel(Level.parse(handlerLevel));
        } catch (IllegalArgumentException e) {
            defaultLogger.log(Level.WARNING, "handlerLevel is invalid", e);
        }
        logger.addHandler(handler);
    }

    public Logger getDefaultLogger() {
        return defaultLogger;
    }

    public Logger getLogger() {
        return logger;
    }
}

package io.wcheng.dataimporter.file;

import java.nio.charset.Charset;

/**
 * POJO for file settings
 */
public class FileSettings {
    private String filePath;
    private char delimiter;
    private char qualifier;
    private Charset encoding;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public char getQualifier() {
        return qualifier;
    }

    public void setQualifier(char qualifier) {
        this.qualifier = qualifier;
    }

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }
}

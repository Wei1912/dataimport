package io.wcheng.dataimporter.file;

import io.wcheng.dataimporter.common.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static int SAMPLE_DATA_MAX_CNT = 5;

    private String fileType;
    private List<String> headers;
    private List<String[]> sampleData;
    private FileSettings fileSettings;

    public List<String> getHeaders() {
        if (headers == null) {
            try {
                readTextFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return headers;
    }

    public List<String[]> getSampleData() {
        if (sampleData == null) {
            try {
                readTextFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sampleData;
    }

    public FileSettings getFileSettings() {
        return fileSettings;
    }

    public void setFileSettings(FileSettings fileSettings) {
        this.fileSettings = fileSettings;
    }

    /**
     * Read headers and up to 5 rows sample data from text file.
     */
    private void readTextFile() throws IOException {
        headers = new ArrayList<>();
        sampleData = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileSettings.getFilePath(), fileSettings.getEncoding()));
        int i = 0;
        String line = null;
        while ((line = br.readLine()) != null) {
            List<String> data = StringUtils.parseTextLine(line, fileSettings.getDelimiter(), fileSettings.getQualifier());
            if (i == 0) {
                headers = data;
            } else {
                String[] dataArray = new String[headers.size()];
                data.toArray(dataArray);
                sampleData.add(dataArray);
            }
            i++;
            if (i == (SAMPLE_DATA_MAX_CNT + 1)) break;
        }
        br.close();
    }
}

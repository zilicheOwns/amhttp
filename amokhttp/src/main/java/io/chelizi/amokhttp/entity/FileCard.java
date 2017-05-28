package io.chelizi.amokhttp.entity;

/**
 * Created by Eddie on 2017/5/28.
 */

public class FileCard {

    private String destDir;
    private String destName;

    public FileCard(String destDir, String destName) {
        this.destDir = destDir;
        this.destName = destName;
    }

    public String getDestDir() {
        return destDir;
    }

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }
}

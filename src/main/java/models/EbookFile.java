package models;

import java.io.Serializable;

public class EbookFile extends Base implements Serializable {
    private int ebookId;
    private int fileId;
    private boolean isDefault;

    public EbookFile() {
        super(-1);
    }

    public EbookFile(int ebookId, int fileId, boolean isDefault) {
        super(-1);
        this.ebookId = ebookId;
        this.fileId = fileId;
        this.isDefault = isDefault;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}

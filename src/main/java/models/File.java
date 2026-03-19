package models;

public class File extends Base {
    private String fileName;
    private String fileFormat;
    private long fileSize;
    private String fileLink;
    private String fileStatus;

    public File(String fileName, String fileFormat, long fileSize, String fileLink, String fileStatus) {
        super(-1);
        this.fileName = fileName;
        this.fileFormat = fileFormat;
        this.fileSize = fileSize;
        this.fileLink = fileLink;
        this.fileStatus = fileStatus;
    }

    public File(int id) {
        super(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }
}

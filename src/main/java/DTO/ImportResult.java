package DTO;

public class ImportResult {
    private int success;
    private int skipped;

    public ImportResult(int success, int skipped) {
        this.success = success;
        this.skipped = skipped;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }
}

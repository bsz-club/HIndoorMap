package com.bsz.hanyue.androiddownloader;

/**
 * Created by hanyue on 2015/8/11
 */
public class DownloadException extends Exception {
    /**
     * The context is NULL.
     */
    public static final int CONTEXT_NOT_VALID = 2;
    /**
     * The task failed.
     */
    public static final int DOWNLOAD_TASK_FAILED = 11;
    /**
     * The task is not valid.
     */
    public static final int DOWNLOAD_TASK_NOT_VALID = 1;
    /**
     * the operation is not valid.
     */
    public static final int OPERATION_NOT_VALID = 10;
    /**
     * Unknown Error
     */
    public static final int UNKNOWN = 0;
    /**
     * serialVersionUID *
     */
    private static final long serialVersionUID = -3804737584048978515L;
    private int code = UNKNOWN;

    public DownloadException() {
        super(generateMessageFromCode(UNKNOWN));
        this.code = UNKNOWN;
    }

    /**
     * Generage the error message with the code
     *
     * @param code code
     */
    private static String generateMessageFromCode(int code) {
        String message;

        switch (code) {
            case DOWNLOAD_TASK_NOT_VALID:
                message = "The DownloadTask is null or not valid!";
                break;
            case CONTEXT_NOT_VALID:
                message = "The Context is null or not valid!";
                break;
            case OPERATION_NOT_VALID:
                message = "The operation is not allowed!";
                break;
            case DOWNLOAD_TASK_FAILED:
                message = "The Download Task is failed!";
                break;
            case UNKNOWN:
            default:
                message = "Unknown Error!";
                break;
        }
        return message;
    }

    public DownloadException(int code) {
        super(generateMessageFromCode(code));
        this.code = code;
    }

    public DownloadException(String message) {
        super(message);
        this.code = UNKNOWN;
    }

    public DownloadException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

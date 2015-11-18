package com.xyj.common.tool.upload.spring;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:13-10-29 下午5:50
 */
//ignore "bytes" when return json format

@JsonIgnoreProperties({"bytes"})
public class FileMeta implements Serializable{
    private String fileName;
    private String fileSize;
    private String fileType;

    private byte[] bytes;


    public FileMeta(){

    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }


}

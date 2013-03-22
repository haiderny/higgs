package io.higgs.http.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpFile {
    protected Logger log = LoggerFactory.getLogger(getClass());
    private final String name;
    private final boolean isText;
    private final File file;
    private String contentType;

    public HttpFile(String name, File file) {
        this(name, file, false);
    }

    /**
     * Create a reference to a file to be uploaded and configure its associated properties
     *
     * @param name   the name of the parameter
     * @param file   the file to be uploaded (if not Multipart mode, only the filename will be included)
     * @param isText True if this file should be transmitted in Text format (else binary)
     * @author Courtney Robinson <courtney@crlog.info>
     */
    public HttpFile(String name, File file, boolean isText) {
        this(name, file, null, isText);
        contentType(name, file);
    }

    public HttpFile(String name, File file, String contentType, boolean isText) {
        this.name = name;
        this.file = file;
        this.isText = isText;
        this.contentType = contentType;
    }

    public void contentType(String name, File file) {
        try {
            this.contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            this.contentType = null;
            log.warn(String.format("Unable to get content type for file %s", name), e);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isText() {
        return isText;
    }

    public File getFile() {
        return file;
    }

    public String getContentType() {
        return contentType;
    }
}
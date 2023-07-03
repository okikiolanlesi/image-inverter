package com.upload.api;

import play.mvc.Http;

import java.io.File;

public interface IUpload {
    String uploadImage(Http.MultipartFormData.FilePart file);
    File getImage(String filePath);

}

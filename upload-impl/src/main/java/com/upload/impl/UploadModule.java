package com.upload.impl;

import com.upload.api.IUpload;
import com.google.inject.AbstractModule;

public class UploadModule extends AbstractModule {

    @Override
    protected void configure(){
        bind(IUpload.class).to(DefaultUploadImpl.class);
    }

}

package com.upload.impl;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.dispatch.MessageDispatcher;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Flow;
import akka.util.ByteString;
import com.upload.api.IUpload;
import com.google.inject.Inject;
import play.db.jpa.JPAApi;
import play.mvc.Http;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class DefaultUploadImpl implements IUpload {
    private JPAApi jpaApi;
    private String imagesDirectory = "images";

    ActorSystem actorSystem;
    Materializer materializer;
    private MessageDispatcher dispatcher;

    @Inject
    public DefaultUploadImpl(JPAApi jpaApi, ActorSystem actorSystem, Materializer materializer){
        this.jpaApi = jpaApi;
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.dispatcher = actorSystem.dispatchers().lookup("akka.stream.default-blocking-io-dispatcher");

    }

    @Override
    public String uploadImage(Http.MultipartFormData.FilePart image) {
                String filename = image.getFilename() + UUID.randomUUID().toString();
                File temporaryFile = (File) image.getFile();
                File destinationFile = new File(imagesDirectory, filename);

                // Invert image colors and save
                invertImageColors(temporaryFile, destinationFile);

                return "Image uploaded and colors inverted. filePath: '" + destinationFile.getAbsolutePath() + "'";
    };

    @Override
    public File getImage(String imagePath){
        File file = new File(imagesDirectory, imagePath);
        if (!file.exists()) {
            return null;
        }

        return file;
    };



    public CompletionStage<File> invertImageColors(File inputImage, File outputImage) {
        Flow<ByteString, ByteString, NotUsed> invertColorsFlow = Flow.<ByteString>create()
                .mapAsync(Runtime.getRuntime().availableProcessors(), this::invertPixelColors)
                .withAttributes(akka.stream.ActorAttributes.dispatcher(dispatcher.toString()));

        return FileIO.fromPath(inputImage.toPath())
                .via(invertColorsFlow)
                .runWith(FileIO.toPath(outputImage.toPath()), materializer)
                .thenApply(done -> outputImage);
    }

    private CompletionStage<ByteString> invertPixelColors(ByteString byteString) {
        return CompletableFuture.supplyAsync(() -> {
            byte[] byteArray = byteString.toArray();
            int length = byteArray.length;

            for (int i = 0; i < length; i += 3) {
                byte red = byteArray[i];
                byte green = byteArray[i + 1];
                byte blue = byteArray[i + 2];

                byteArray[i] = green;
                byteArray[i + 1] = blue;
                byteArray[i + 2] = red;
            }

            return ByteString.fromArray(byteArray);
        }, dispatcher);
    }
}
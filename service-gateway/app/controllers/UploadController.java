package controllers;

import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.Materializer;
import com.upload.api.IUpload;
import com.encentral.scaffold.commons.utils.MyObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@Transactional
@Api(value = "Employee")
public class UploadController extends Controller {

    @Inject
    IUpload iUpload;

    @Inject
    FormFactory formFactory;

    @Inject
    MyObjectMapper objectMapper;

    @ApiOperation(value = "Upload an image")
    @ApiResponses(
            value= {@ApiResponse(code = 200, response = String.class, message="Image uploaded succcessfully")}
    )
    public Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFile("image");

        if(image == null){
            return badRequest("No image provided");
        }

        if (!image.getContentType().startsWith("image/")) {
            return badRequest("Only images are allowed");
        }

        String response = iUpload.uploadImage(image);

        return ok(response);

}


    public Result getFile(String imagePath) {
        File file = iUpload.getImage(imagePath);

        if(file == null){
            return notFound("Image not found");
        }

        return ok(file);
    }
}

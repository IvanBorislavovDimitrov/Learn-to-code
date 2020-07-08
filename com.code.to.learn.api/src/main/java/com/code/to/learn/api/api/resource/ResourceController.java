package com.code.to.learn.api.api.resource;

import com.code.to.learn.api.api.course.CourseServiceApi;
import com.code.to.learn.api.model.course.CourseResponseModel;
import com.code.to.learn.api.util.RangeHeaderGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static com.code.to.learn.api.constant.Constants.RESOURCE_BUFFER_SIZE;

@Controller
@RequestMapping(value = "/resource")
public class ResourceController {

    private final ResourceServiceApi resourceServiceApi;
    private final CourseServiceApi courseServiceApi;

    @Autowired
    public ResourceController(ResourceServiceApi resourceServiceApi, CourseServiceApi courseServiceApi) {
        this.resourceServiceApi = resourceServiceApi;
        this.courseServiceApi = courseServiceApi;
    }

    @GetMapping(value = "/images/{name:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImageResource(@PathVariable String name) {
        return resourceServiceApi.getImageResource(name);
    }

    @GetMapping(value = "/videos/{courseName}/{name:.*}", produces = "video/mp4")
    public CompletableFuture<StreamingResponseBody> getVideoResource(@PathVariable String courseName,
                                                                     @PathVariable String name,
                                                                     HttpServletRequest request,
                                                                     HttpServletResponse response) {
        RangeHeaderGetter rangeHeaderGetter = RangeHeaderGetter.createRangeHeaderGetter(request.getHeader(HttpHeaders.RANGE));
        response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + rangeHeaderGetter.getOffset() + "-" +
                (rangeHeaderGetter.getOffset()) + "/" + (getVideoFileSize(courseName, name)));
        response.setHeader(HttpHeaders.ACCEPT_RANGES, " bytes");

        response.setStatus(HttpStatus.PARTIAL_CONTENT.value());

        InputStream videoInputStream = resourceServiceApi.openFileStream(name, rangeHeaderGetter.getOffset());
        BufferedInputStream bufferedVideoInputStream = new BufferedInputStream(videoInputStream);

        StreamingResponseBody streamingResponseBody = outputStream -> {
            byte[] read = new byte[RESOURCE_BUFFER_SIZE];
            int len;
            while ((len = bufferedVideoInputStream.read(read)) != -1) {
                outputStream.write(read, 0, len);
            }
        };
        return CompletableFuture.completedFuture(streamingResponseBody);
    }

    private long getVideoFileSize(String courseName, String videoName) {
        return courseServiceApi.getVideoSize(courseName, videoName);
    }

}

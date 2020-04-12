package com.code.to.learn.api.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
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

    @Autowired
    public ResourceController(ResourceServiceApi resourceServiceApi) {
        this.resourceServiceApi = resourceServiceApi;
    }

    @GetMapping(value = "/images/{name:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImageResource(@PathVariable String name) {
        return resourceServiceApi.getImageResource(name);
    }

    @GetMapping(value = "/videos/{name:.*}", produces = "video/mp4")
    @Async
    public CompletableFuture<StreamingResponseBody> getVideoResource(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        InputStream videoInputStream = resourceServiceApi.openFileStream(name);
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

}

package com.code.to.learn.api.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}

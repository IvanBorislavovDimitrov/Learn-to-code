package com.code.to.learn.api.api.general;

import com.code.to.learn.api.constant.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralRestController {

    @GetMapping(value = "/")
    public ResponseEntity<String> pong() {
        return ResponseEntity.ok(Constants.PONG);
    }

}

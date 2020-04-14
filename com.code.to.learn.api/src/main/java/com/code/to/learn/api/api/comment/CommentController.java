package com.code.to.learn.api.api.comment;

import com.code.to.learn.api.model.comment.CommentBindingModel;
import com.code.to.learn.api.model.comment.CommentResponseModel;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {

    private final CommentServiceApi commentServiceApi;
    private final UsernameGetter usernameGetter;

    @Autowired
    public CommentController(CommentServiceApi commentServiceApi, UsernameGetter usernameGetter) {
        this.commentServiceApi = commentServiceApi;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponseModel> add(@RequestBody @Valid CommentBindingModel commentBindingModel) {
        commentBindingModel.setAuthor(usernameGetter.getLoggedInUserUsername());
        return commentServiceApi.add(commentBindingModel);
    }

}

package com.code.to.learn.api.api.comment;

import com.code.to.learn.api.model.comment.CommentBindingModel;
import com.code.to.learn.api.model.comment.CommentResponseModel;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

    private final CommentServiceApi commentServiceApi;
    private final UsernameGetter usernameGetter;

    @Autowired
    public CommentController(CommentServiceApi commentServiceApi, UsernameGetter usernameGetter) {
        this.commentServiceApi = commentServiceApi;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<CommentResponseModel> add(@RequestBody @Valid CommentBindingModel commentBindingModel) {
        commentBindingModel.setAuthor(usernameGetter.getLoggedInUserUsername());
        return commentServiceApi.add(commentBindingModel);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<CommentResponseModel>> getCommentsByCourseName(@RequestParam String courseName) {
        return commentServiceApi.getCommentsByCourseName(courseName);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<CommentResponseModel> edit(@RequestBody @Valid CommentBindingModel commentBindingModel) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return commentServiceApi.update(commentBindingModel, loggedUser);
    }

    @DeleteMapping(value = "/delete/{commentId}")
    public ResponseEntity<CommentResponseModel> delete(@PathVariable String commentId) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return commentServiceApi.delete(commentId, loggedUser);
    }

    @GetMapping(value = "/{commentId}")
    public ResponseEntity<CommentResponseModel> get(@PathVariable String commentId) {
        return commentServiceApi.get(commentId);
    }

}

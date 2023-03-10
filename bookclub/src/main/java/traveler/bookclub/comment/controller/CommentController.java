package traveler.bookclub.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import traveler.bookclub.comment.dto.CommentResponse;
import traveler.bookclub.comment.dto.CommentSaveRequest;
import traveler.bookclub.comment.dto.CommentUpdateRequest;
import traveler.bookclub.comment.dto.MyCommentListDto;
import traveler.bookclub.comment.service.CommentService;
import traveler.bookclub.common.response.StringResponse;

import java.util.List;

@RequestMapping("/comments")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public StringResponse saveComment(@Valid @RequestBody CommentSaveRequest request) {
        Long commentId = commentService.saveComment(request);
        return new StringResponse("댓글을 성공적으로 저장했습니다. 댓글 ID: " + commentId.toString());
    }

    @GetMapping("/{reviewId}")
    public List<CommentResponse> readComments(@PathVariable Long reviewId) {
        return commentService.readCommentsByReview(reviewId);
    }

    @GetMapping("/me")
    public List<MyCommentListDto> readMyComments() {
        return commentService.readMyComments();
    }

    @PutMapping
    public StringResponse updateComment(@Valid @RequestBody CommentUpdateRequest request) {
        commentService.updateComment(request);
        return new StringResponse("댓글을 성공적으로 수정했습니다.");
    }
}

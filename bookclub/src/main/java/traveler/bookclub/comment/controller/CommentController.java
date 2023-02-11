package traveler.bookclub.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import traveler.bookclub.comment.dto.CommentSaveRequest;
import traveler.bookclub.comment.service.CommentService;
import traveler.bookclub.common.response.StringResponse;

@RequestMapping("/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public StringResponse saveComment(CommentSaveRequest request) {
        Long commentId = commentService.saveComment(request);
        return new StringResponse("댓글을 성공적으로 저장했습니다. 댓글 ID: " + commentId.toString());
    }
}

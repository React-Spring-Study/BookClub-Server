package traveler.bookclub.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.comment.domain.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyCommentListDto {
    private String content;
    private Long reviewId;
    private String createdDate;

    public static List<MyCommentListDto> of(List<Comment> commentList) {
        List<MyCommentListDto> dtos = new ArrayList<>();
        for (Comment comment : commentList) {
            dtos.add(new MyCommentListDto(
                    comment.getContent(), comment.getReview().getId(), comment.getCreatedDate()
            ));
        }
        return dtos;
    }
}

package faang.school.postservice.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotNull(message = "Content can't be empty")
    @Size(min = 1, max = 4096, message = "Content should be at least 1 symbol long and max 4096 symbols")
    private String content;
    @NotNull(message = "AuthorId can't be empty")
    private Long authorId;
    private Long likesNum;
    @NotNull(message = "PostId can't be empty")
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

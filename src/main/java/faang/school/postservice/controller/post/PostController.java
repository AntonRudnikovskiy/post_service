package faang.school.postservice.controller.post;

import faang.school.postservice.dto.post.CreatePostDto;
import faang.school.postservice.dto.post.ResponsePostDto;
import faang.school.postservice.dto.post.UpdatePostDto;
import faang.school.postservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/")
public class PostController {
    private final PostService postService;

    @GetMapping("draft/by-author/{authorId}")
    public List<ResponsePostDto> getAllDraftByAuthor(@PathVariable Long authorId) {
        return postService.getAllDraftByAuthor(authorId);
    }

    @GetMapping("published/by-author/{authorId}")
    public List<ResponsePostDto> getAllPublishedByAuthor(@PathVariable Long authorId) {
        return postService.getAllPublishedByAuthor(authorId);
    }

    @GetMapping("draft/by-project/{projectId}")
    public List<ResponsePostDto> getAllDraftByProject(@PathVariable Long projectId) {
        return postService.getAllDraftByProject(projectId);
    }

    @GetMapping("published/by-project/{projectId}")
    public List<ResponsePostDto> getAllPublishedByProject(@PathVariable Long projectId) {
        return postService.getAllPublishedByProject(projectId);
    }

    @PostMapping("{postId}/publish")
    public ResponsePostDto publish(@PathVariable Long postId) {
        return postService.publish(postId);
    }

    @PutMapping
    public ResponsePostDto update(UpdatePostDto dto) {
        return postService.update(dto);
    }

    @PutMapping("/like")
    public ResponsePostDto likePost(@RequestHeader("x-user-id") Long userId, UpdatePostDto dto) {
        return postService.likePost(dto, userId);
    }

    @GetMapping("{id}")
    public ResponsePostDto getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @DeleteMapping("{id}")
    public ResponsePostDto softDelete(@PathVariable Long id) {
        return postService.softDelete(id);
    }

    @PostMapping
    public ResponsePostDto createDraft(@Valid @RequestBody CreatePostDto dto) {
        return postService.createDraft(dto);
    }

    @GetMapping("/hashtags/{hashtag}/firstNew")
    public List<ResponsePostDto> getPostsByHashtagOrderByDate(@PathVariable String hashtag) {
        return postService.getPostsByHashtagOrderByDate(hashtag);
    }

    @GetMapping("/hashtags/{hashtag}/firstPopular")
    public List<ResponsePostDto> getPostsByHashtagOrderByPopularity(@PathVariable String hashtag) {
        return postService.getPostsByHashtagOrderByPopularity(hashtag);
    }
}

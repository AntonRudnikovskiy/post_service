package faang.school.postservice.service;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.LikeDto;
import faang.school.postservice.exceptions.DataNotFoundException;
import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.exceptions.SameTimeActionException;
import faang.school.postservice.mapper.LikeMapper;
import faang.school.postservice.mapper.LikeMapperImpl;
import faang.school.postservice.model.Comment;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.LikeRepository;
import faang.school.postservice.validator.LikeValidator;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;


    LikeMapper likeMapper;

    @Mock
    private UserServiceClient userServiceClient;

    private LikeValidator likeValidator;

    private LikeDto likeDto;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        likeDto = LikeDto.builder().userId(1L).build();
        userDto = new UserDto(1L, "Andrey", "gmail@gmail.com");
        likeMapper = new LikeMapperImpl();
        likeValidator = new LikeValidator(userServiceClient);
        likeService = new LikeService(likeValidator, likeMapper, likeRepository, postService, commentService);
    }

    @Test
    void testLikePost() {
        likeDto.setPostId(1L);

        when(userServiceClient.getUser(1L)).thenReturn(userDto);

        Post post = Post.builder().id(1L).build();
        Mockito.when(postService.getPost(1L)).thenReturn(post);

        Like like = Like.builder().id(0L).userId(1L).post(post).build();

        assertEquals(likeMapper.toDto(like), likeService.likePost(likeDto));
        verify(likeRepository).save(like);
    }

    @Test
    void testWhenUserDoesNotExistOnLikingPost() {
        Long userId = 1L;
        when(userServiceClient.getUser(userId)).thenThrow(FeignException.class);
        DataNotFoundException dataNotExistingException =
                assertThrows(DataNotFoundException.class, () -> likeService.likePost(likeDto));

        assertEquals(String.format("User with id=%d doesn't exist", userId), dataNotExistingException.getMessage());
    }

    @Test
    void testWhenAddLikeOnPostAndComment() {
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        likeDto.setCommentId(1L);
        likeDto.setPostId(1L);
        SameTimeActionException sameTimeActionException =
                assertThrows(SameTimeActionException.class, () -> likeService.likePost(likeDto));

        assertEquals("Can't add like on post and comment in the same time",
                sameTimeActionException.getMessage());
    }

    @Test
    void testUnlikePost() {
        likeService.unlikePost(1L, 1L);
        verify(likeRepository).deleteByPostIdAndUserId(1L, 1L);
    }

    @Test
    void testLikeComment () {
        likeDto.setCommentId(1L);

        when(userServiceClient.getUser(1L)).thenReturn(userDto);

        Comment comment = Comment.builder().id(1L).build();
        when(commentService.getComment(1L)).thenReturn(comment);


        Like like = Like.builder().id(0L).userId(1L).comment(comment).build();

        assertEquals(likeMapper.toDto(like), likeService.likeComment(likeDto));
        verify(likeRepository).save(like);
    }

    @Test
    void testWhenUserDoesNotExistOnLikingComment() {
        Long userId = 1L;
        when(userServiceClient.getUser(userId)).thenThrow(FeignException.class);
        DataNotFoundException dataNotExistingException =
                assertThrows(DataNotFoundException.class, () -> likeService.likeComment(likeDto));

        assertEquals(String.format("User with id=%d doesn't exist", userId), dataNotExistingException.getMessage());
    }

    @Test
    void testUnlikeComment() {
        likeService.unlikeComment(1L, 1L);
        verify(likeRepository).deleteByCommentIdAndUserId(1L, 1L);
    }
}
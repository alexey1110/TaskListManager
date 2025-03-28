package com.example.taskListManager.Service;

import com.example.taskListManager.DTO.CommentDTO;
import com.example.taskListManager.Exceptions.NoAccessRightsException;
import com.example.taskListManager.Model.Comment;
import com.example.taskListManager.Model.Role;
import com.example.taskListManager.Model.Task;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.CommentRepository;
import com.example.taskListManager.Repository.TaskRepository;
import com.example.taskListManager.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private Task task;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTaskId(1L);

        user = new User();
        user.setUserId(1L);
        user.setUserRole(Role.USER);
        user.setAssignedTasks(Collections.singletonList(task));


        comment = new Comment();
        comment.setCommentId(1L);
        comment.setCommentText("Test comment");
        comment.setTask(task);
        comment.setCommentAuthor(user);
    }

    @Test
    void addComment_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO result = commentService.addComment(1L, 1L, "Test comment");

        assertNotNull(result);
        assertEquals("Test comment", result.getCommentText());
        assertEquals(1L, result.getTaskId());
        assertEquals(1L, result.getUserId());

        verify(taskRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_NoAccessRights_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        user.setAssignedTasks(Collections.emptyList());

        NoAccessRightsException exception = assertThrows(NoAccessRightsException.class,
                () -> commentService.addComment(1L, 1L, "Test comment"));
        assertEquals("No access rights", exception.getMessage());

        verify(taskRepository).findById(1L);
        verify(userRepository).findById(1L);
        verifyNoInteractions(commentRepository);
    }

    @Test
    void getAllCommentByTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.findByTask_TaskId(1L)).thenReturn(Collections.singletonList(comment));

        var result = commentService.getAllCommentByTask(1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getCommentText());

        verify(taskRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(commentRepository).findByTask_TaskId(1L);
    }

    @Test
    void deleteComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository).findById(1L);
        verify(commentRepository).deleteById(1L);
    }
}
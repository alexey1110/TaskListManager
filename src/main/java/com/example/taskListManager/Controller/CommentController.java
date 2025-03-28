package com.example.taskListManager.Controller;

import com.example.taskListManager.DTO.CommentDTO;
import com.example.taskListManager.DTO.Request.TextCommentDTORequest;
import com.example.taskListManager.DTO.UserDTO;
import com.example.taskListManager.Service.CommentService;
import com.example.taskListManager.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Комментарии", description = "Методы для работы с комментариями к задачам")
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    private UserDTO getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }

    @Operation(
            summary = "Добавление комментария",
            description = "Позволяет администратору или пользователю добавить комментарий к задаче."
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public CommentDTO createComment(@PathVariable Long taskId,
                                    @RequestBody @Valid TextCommentDTORequest request){
        UserDTO user = getCurrentUser();
        return commentService.addComment(taskId, user.getUserId(), request.getText());
    }

    @Operation(
            summary = "Получение всех комментариев задачи",
            description = "Позволяет администратору или пользователю получить список всех комментариев для конкретной задачи."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<CommentDTO> getAllCommentsByTask(@PathVariable Long taskId){
        UserDTO user = getCurrentUser();
        return commentService.getAllCommentByTask(taskId, user.getUserId());
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Позволяет администратору удалить комментарий по его ID."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void  deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }
}

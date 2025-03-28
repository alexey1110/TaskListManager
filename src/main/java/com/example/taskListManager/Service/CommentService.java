package com.example.taskListManager.Service;

import com.example.taskListManager.DTO.CommentDTO;
import com.example.taskListManager.Exceptions.NoAccessRightsException;
import com.example.taskListManager.Exceptions.TaskNotFoundException;
import com.example.taskListManager.Model.Comment;
import com.example.taskListManager.Model.Role;
import com.example.taskListManager.Model.Task;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.CommentRepository;
import com.example.taskListManager.Repository.TaskRepository;
import com.example.taskListManager.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private CommentDTO convertToDTO(Comment comment){
        return new CommentDTO(comment.getCommentId(),
                comment.getCommentText(),
                comment.getTask().getTaskId(),
                comment.getCommentAuthor().getUserId());
    }

    @Transactional
    public CommentDTO addComment(Long taskId, Long userId, String text){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!user.getAssignedTasks().stream()
                .anyMatch(t -> t.getTaskId().equals(taskId)) && !user.getUserRole().equals(Role.ADMIN)){
            throw new NoAccessRightsException("No access rights");
        }
        Comment comment = new Comment();
        comment.setCommentText(text);
        comment.setCommentAuthor(user);
        comment.setTask(task);

        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    public List<CommentDTO> getAllCommentByTask(Long taskId, Long userId){
        taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!user.getAssignedTasks().stream()
                .anyMatch(t -> t.getTaskId().equals(taskId)) && !user.getUserRole().equals(Role.ADMIN)){
            throw new NoAccessRightsException("No access rights");
        }
        return commentRepository.findByTask_TaskId(taskId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment( Long commentId){
        commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));
        commentRepository.deleteById(commentId);
    }
}

package com.example.taskListManager.Repository;

import com.example.taskListManager.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask_TaskId(Long taskId);
}

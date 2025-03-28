package com.example.taskListManager.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "COMMENT", schema = "public")
@Getter
@Setter
public class Comment {
    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "COMMENT_TEXT")
    private String commentText;

    @ManyToOne
    @JoinColumn(name = "TASK_ID", referencedColumnName = "TASK_ID", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    private User commentAuthor;
}

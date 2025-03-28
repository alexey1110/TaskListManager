package com.example.taskListManager.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TASK", schema = "public")
@Getter
@Setter
public class Task {
    @Id
    @Column(name = "TASK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(name = "TASK_TITLE", nullable = false)
    private String taskTitle;

    @Column(name = "TASK_DESC",nullable = false, length = 1000)
    private String taskDescription;

    @Column(name = "TASK_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status taskStatus;

    @Column(name = "TASK_PRIORITY",nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority taskPriority;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "USER_ID", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "EXECUTOR_ID", referencedColumnName = "USER_ID")
    private User executor;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;
}

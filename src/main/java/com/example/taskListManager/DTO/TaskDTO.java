package com.example.taskListManager.DTO;

import com.example.taskListManager.Model.Priority;
import com.example.taskListManager.Model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long taskId;
    private String taskTitle;
    private String taskDescription;
    private Status taskStatus;
    private Priority taskPriority;
    private Long authorId;
    private Long executorId;
}

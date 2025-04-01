package com.example.taskListManager.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDTORequest {
    @NotBlank(message = "Название задачи не может быть пустым")
    private String title;

    @NotBlank(message = "Описание задачи не может быть пустым")
    private String description;
}

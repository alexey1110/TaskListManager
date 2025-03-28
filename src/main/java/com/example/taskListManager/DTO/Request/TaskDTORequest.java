package com.example.taskListManager.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTORequest {
    @NotBlank(message = "Название задачи не может быть пустым")
    @Size(min = 1, max = 100, message = "Название должно содержать от 1 до 100 символов")
    private String title;

    @NotBlank(message = "Описание задачи не может быть пустым")
    @Size(min = 1, max = 500, message = "Описание должно содержать от 1 до 500 символов")
    private String description;
}

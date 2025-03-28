package com.example.taskListManager.DTO.Request;

import com.example.taskListManager.Model.Priority;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriorityDTORequest {
    @NotEmpty(message = "Приоритет задачи не может быть пустым")
    private Priority priority;
}

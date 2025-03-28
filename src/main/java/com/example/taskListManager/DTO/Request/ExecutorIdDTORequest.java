package com.example.taskListManager.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutorIdDTORequest {
    @NotNull(message = "ID исполнителя не может быть пустым")
    @Min(value = 1, message = "ID исполнителя должен быть больше 0")
    private Long executorId;
}

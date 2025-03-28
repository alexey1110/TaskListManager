package com.example.taskListManager.DTO.Request;

import com.example.taskListManager.Model.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDTORequest {
    @NotEmpty(message = "Статус задачи не может быть пустым")
    private Status status;
}

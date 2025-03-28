package com.example.taskListManager.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextCommentDTORequest {
    @NotBlank(message = "Описание задачи не может быть пустым")
    @Size(min = 1, max = 1000, message = "Описание должно содержать от 1 до 1000 символов")
    private String text;
}

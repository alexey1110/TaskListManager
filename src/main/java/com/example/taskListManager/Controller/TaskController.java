package com.example.taskListManager.Controller;

import com.example.taskListManager.DTO.Request.ExecutorIdDTORequest;
import com.example.taskListManager.DTO.Request.PriorityDTORequest;
import com.example.taskListManager.DTO.Request.StatusDTORequest;
import com.example.taskListManager.DTO.TaskDTO;
import com.example.taskListManager.DTO.Request.TaskDTORequest;
import com.example.taskListManager.DTO.UserDTO;
import com.example.taskListManager.Model.Priority;
import com.example.taskListManager.Model.Status;
import com.example.taskListManager.Service.TaskService;
import com.example.taskListManager.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Задачи", description = "Управление задачами")
@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController (TaskService taskService, UserService userService){
        this.taskService = taskService;
        this.userService = userService;
    }

    private UserDTO getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }

    @Operation(
            summary = "Получить все задачи исполнителя",
            description = "Возвращает список задач, где текущий пользователь является исполнителем. Доступно для пользователей и администраторов.")
    @GetMapping("/my-tasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<TaskDTO> getAllExecTasks(@RequestParam(required = false) Status status,
                                         @RequestParam(required = false) Priority priority,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int size){
        UserDTO user = getCurrentUser();
        return taskService.getAllTasksByExecutor(user.getUserId(), status, priority, page, size);
    }

    @Operation(
            summary = "Получить задачу исполнителя по ID",
            description = "Возвращает конкретную задачу, где текущий пользователь является исполнителем. Доступно для пользователей и администраторов.")
    @GetMapping("/my-tasks/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskDTO getExecTaskById(@PathVariable Long taskId){
        UserDTO user = getCurrentUser();
        return taskService.getAssignedTaskById(user.getUserId(), taskId);
    }

    @Operation(
            summary = "Получить все задачи",
            description = "Возвращает все задачи в системе. Доступно только для администраторов.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<TaskDTO> getAllTasks(@RequestParam(required = false) Status status,
                                     @RequestParam(required = false) Priority priority,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size){
        return taskService.getAllTask(status, priority, page, size);
    }

    @Operation(
            summary = "Получить задачи конкретного автора",
            description = "Возвращает список задач, созданных указанным пользователем. Доступно только для администраторов.")
    @GetMapping("/author/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<TaskDTO> getAllTaskByAuthor(@PathVariable Long userId,
                                            @RequestParam(required = false) Status status,
                                            @RequestParam(required = false) Priority priority,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size){
        return taskService.getAllTasksByAuthor(userId, status, priority, page, size);
    }

    @Operation(
            summary = "Получить задачи конкретного исполнителя",
            description = "Возвращает список задач, назначенных указанному исполнителю. Доступно только для администраторов.")
    @GetMapping("/executor/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<TaskDTO> getAllTaskByExecutor(@PathVariable Long userId,
                                              @RequestParam(required = false) Status status,
                                              @RequestParam(required = false) Priority priority,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size){
        return taskService.getAllTasksByExecutor(userId, status, priority, page, size);
    }

    @Operation(
            summary = "Создать новую задачу",
            description = "Создает новую задачу. Доступно только для администраторов.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TaskDTO createTask(@RequestBody @Valid TaskDTORequest request){
        UserDTO user = getCurrentUser();
        return taskService.createTask(user.getUserId(), request.getTitle(), request.getDescription());
    }

    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает задачу по ее ID. Доступно только для администраторов.")
    @GetMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskDTO getTask(@PathVariable Long taskId){
        return taskService.getTaskById(taskId);
    }

    @Operation(
            summary = "Обновить задачу",
            description = "Обновляет заголовок и описание существующей задачи. Доступно только для администраторов.")
    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskDTO updateTask(@PathVariable Long taskId,
                              @RequestBody @Valid TaskDTORequest request){
        return taskService.updateTask(taskId, request.getTitle(), request.getDescription());
    }

    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по ее ID. Доступно только для администраторов.")
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTask(@PathVariable Long taskId){
        taskService.deleteTask(taskId);
    }

    @Operation(
            summary = "Обновить статус задачи",
            description = "Изменяет статус существующей задачи. Доступно для пользователей и администраторов.")
    @PatchMapping("/{taskId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskDTO updateTaskStatus(@PathVariable Long taskId,
                                    @RequestBody @Valid StatusDTORequest request){
        return taskService.updateStatus(taskId, request.getStatus());
    }

    @Operation(
            summary = "Обновить приоритет задачи",
            description = "Изменяет приоритет существующей задачи. Доступно только для администраторов.")
    @PatchMapping("/{taskId}/priority")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskDTO updateTaskPriority(@PathVariable Long taskId,
                                      @RequestBody @Valid PriorityDTORequest request){
        return taskService.updatePriority(taskId, request.getPriority());
    }

    @Operation(
            summary = "Назначить исполнителя",
            description = "Назначает исполнителя на задачу. Доступно только для администраторов.")
    @PatchMapping("/{taskId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskDTO assignExecutor(@PathVariable Long taskId,
                                  @RequestBody @Valid ExecutorIdDTORequest request){
        return taskService.assignExecutor(taskId, request.getExecutorId());
    }
}

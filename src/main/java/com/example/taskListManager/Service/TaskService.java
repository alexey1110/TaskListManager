package com.example.taskListManager.Service;

import com.example.taskListManager.DTO.TaskDTO;
import com.example.taskListManager.Exceptions.TaskNotFoundException;
import com.example.taskListManager.Exceptions.UserNotFoundException;
import com.example.taskListManager.Model.Priority;
import com.example.taskListManager.Model.Status;
import com.example.taskListManager.Model.Task;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.TaskRepository;
import com.example.taskListManager.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDTO convertToDTO(Task task){
        return new TaskDTO(task.getTaskId(),
                task.getTaskTitle(),
                task.getTaskDescription(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getAuthor().getUserId(),
                task.getExecutor() != null ? task.getExecutor().getUserId() : null);
    }

    private Specification<Task> buildTaskSpecification(
            Status status,
            Priority priority,
            Long authorId,
            Long executorId) {
        Specification<Task> spec = Specification.where(null);
        if (status != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("taskStatus"), status));
        }
        if (priority != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("taskPriority"), priority));
        }
        if (authorId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("author").get("userId"), authorId));
        }
        if (executorId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("executor").get("userId"), executorId));
        }
        return spec;
    }

    public Page<TaskDTO> getFilteredAndPaginatedTasks(Specification<Task> spec, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(spec, pageable).map(this::convertToDTO);
    }
    @Transactional
    public TaskDTO createTask(Long userId, String title, String description){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task = new Task();
        task.setTaskTitle(title);
        task.setTaskDescription(description);
        task.setTaskStatus(Status.PENDING);
        task.setTaskPriority(Priority.MEDIUM);
        task.setAuthor(user);
        return convertToDTO(taskRepository.save(task));
    }

    public Page<TaskDTO> getAllTask(Status status, Priority priority, int page, int size){
        Specification<Task> spec = buildTaskSpecification(status, priority, null, null);
        return getFilteredAndPaginatedTasks(spec, page, size);
    }

    public TaskDTO getTaskById(Long taskId){
        return taskRepository.findById(taskId).stream()
                .map(this::convertToDTO)
                .findFirst()
                .orElseThrow(()->new TaskNotFoundException("Task not found"));
    }

    public Page<TaskDTO> getAllTasksByAuthor(Long userId, Status status, Priority priority, int page, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Author not found"));
        Specification<Task> spec = buildTaskSpecification(status, priority, userId, null);
        return getFilteredAndPaginatedTasks(spec, page, size);
//        return userOpt.get().getCreatedTasks().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
    }

    public Page<TaskDTO> getAllTasksByExecutor(Long userId, Status status, Priority priority, int page, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Executor not found"));
        Specification<Task> spec = buildTaskSpecification(status, priority, null, userId);
        return getFilteredAndPaginatedTasks(spec, page, size);
//        return userOpt.get().getAssignedTasks().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
    }

    public TaskDTO getCreatedTaskById(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getCreatedTasks().stream()
                .filter(task -> taskId.equals(task.getTaskId()))
                .findFirst()
                .map(this::convertToDTO)
                .orElseThrow(()->new TaskNotFoundException("Task not found"));
    }

    public TaskDTO getAssignedTaskById(Long userId, Long taskId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getAssignedTasks().stream()
                .filter(task -> taskId.equals(task.getTaskId()))
                .findFirst()
                .map(this::convertToDTO)
                .orElseThrow(()->new TaskNotFoundException("Task not found"));
    }

    public TaskDTO updateTask(Long taskId, String title, String description){
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTaskTitle(title);
                    task.setTaskDescription(description);
                    return convertToDTO(taskRepository.save(task));
                })
                .orElseThrow(()->new TaskNotFoundException("Task not found"));
    }

    @Transactional
    public void deleteTask(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    public TaskDTO updateStatus(Long taskId, Status status){
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTaskStatus(status);
                    return convertToDTO(taskRepository.save(task));
                })
                .orElseThrow(()->new TaskNotFoundException("Task not found"));
    }

    public TaskDTO updatePriority(Long taskId, Priority priority){
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTaskPriority(priority);
                    return convertToDTO(taskRepository.save(task));
                })
                .orElseThrow(()->new TaskNotFoundException("Task not found"));
    }

    public TaskDTO assignExecutor(Long taskId, Long executorId){
        return taskRepository.findById(taskId)
                .map(task -> userRepository.findById(executorId).
                        map(executor -> {
                            task.setExecutor(executor);
                            return convertToDTO(taskRepository.save(task));
                        })
                        .orElseThrow(() -> new UserNotFoundException("Executor not found")))
                .orElseThrow(()->new TaskNotFoundException("Task not found"));
    }
}

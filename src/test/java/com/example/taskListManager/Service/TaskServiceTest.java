package com.example.taskListManager.Service;
import com.example.taskListManager.Model.Priority;
import com.example.taskListManager.Model.Status;
import com.example.taskListManager.Model.Task;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.TaskRepository;
import com.example.taskListManager.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUserEmail("test@example.com");

        task = new Task();
        task.setTaskId(1L);
        task.setTaskTitle("Test Task");
        task.setTaskDescription("Test Description");
        task.setTaskStatus(Status.PENDING);
        task.setTaskPriority(Priority.MEDIUM);
        task.setAuthor(user);
    }

    @Test
    void createTask_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = taskService.createTask(1L, "Test Task", "Test Description");

        assertNotNull(result);
        assertEquals("Test Task", result.getTaskTitle());
        assertEquals("Test Description", result.getTaskDescription());
        assertEquals(Status.PENDING, result.getTaskStatus());
        assertEquals(Priority.MEDIUM, result.getTaskPriority());

        verify(userRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        var result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTaskTitle());
        assertEquals("Test Description", result.getTaskDescription());

        verify(taskRepository).findById(1L);
    }

    @Test
    void updateTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = taskService.updateTask(1L, "Updated Task", "Updated Description");

        assertNotNull(result);
        assertEquals("Updated Task", result.getTaskTitle());
        assertEquals("Updated Description", result.getTaskDescription());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void assignExecutor_Success() {
        User executor = new User();
        executor.setUserId(2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(executor));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = taskService.assignExecutor(1L, 2L);

        assertNotNull(result);
        assertEquals(2L, result.getExecutorId());

        verify(taskRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(taskRepository).save(any(Task.class));
    }
}
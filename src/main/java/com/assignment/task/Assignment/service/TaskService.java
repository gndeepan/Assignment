package com.assignment.task.Assignment.service;

import com.assignment.task.Assignment.dto.TaskRequestDto;
import com.assignment.task.Assignment.dto.TaskResponseDto;
import com.assignment.task.Assignment.exception.TaskNotFoundException;
import com.assignment.task.Assignment.model.Task;
import com.assignment.task.Assignment.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task task = new Task();
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());

        if (taskRequestDto.getStatus() != null) {
            task.setStatus(taskRequestDto.getStatus());
        }

        task.setDueDate(taskRequestDto.getDueDate());

        Task savedTask = taskRepository.save(task);
        return TaskResponseDto.fromEntity(savedTask);
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return TaskResponseDto.fromEntity(task);
    }

    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        if (taskRequestDto.getTitle() != null && !taskRequestDto.getTitle().isBlank()) {
            existingTask.setTitle(taskRequestDto.getTitle());
        }

        if (taskRequestDto.getDescription() != null) {
            existingTask.setDescription(taskRequestDto.getDescription());
        }

        if (taskRequestDto.getStatus() != null) {
            existingTask.setStatus(taskRequestDto.getStatus());
        }

        if (taskRequestDto.getDueDate() != null) {
            existingTask.setDueDate(taskRequestDto.getDueDate());
        }

        Task updatedTask = taskRepository.save(existingTask);
        return TaskResponseDto.fromEntity(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}
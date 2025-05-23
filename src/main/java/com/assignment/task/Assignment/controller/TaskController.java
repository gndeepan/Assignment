package com.assignment.task.Assignment.controller;

import com.assignment.task.Assignment.dto.TaskRequestDto;
import com.assignment.task.Assignment.dto.TaskResponseDto;
import com.assignment.task.Assignment.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        TaskResponseDto createdTask = taskService.createTask(taskRequestDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto taskRequestDto) {
        TaskResponseDto updatedTask = taskService.updateTask(id, taskRequestDto);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("uh-oh", HttpStatus.OK);
    }
}
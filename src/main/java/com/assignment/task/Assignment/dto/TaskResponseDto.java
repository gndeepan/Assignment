package com.assignment.task.Assignment.dto;

import com.assignment.task.Assignment.model.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {

    private Long id;
    private String title;
    private String description;
    private Task.TaskStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponseDto fromEntity(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}
package com.assignment.task.Assignment.dto;

import com.assignment.task.Assignment.model.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Task.TaskStatus status;

    private LocalDate dueDate;

}
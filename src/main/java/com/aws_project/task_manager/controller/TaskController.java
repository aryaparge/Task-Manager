package com.aws_project.task_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aws_project.task_manager.model.Task;
import com.aws_project.task_manager.repo.TaskRepo;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController
{
    @Autowired
    private TaskRepo taskRepo;

    @GetMapping
    public List<Task> getAllTasks()
    {
        return taskRepo.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task)
    {
        return taskRepo.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask)
    {
        Task task = taskRepo.findById(id).orElseThrow();
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setDueTime(updatedTask.getDueTime());
        task.setPriority(updatedTask.getPriority());
        task.setCompleted(updatedTask.isCompleted());
        return taskRepo.save(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id)
    {
        taskRepo.deleteById(id);
    }

    @PutMapping("/{id}/toggle")
    public Task toggleCompleted(@PathVariable Long id)
    {
        Task task = taskRepo.findById(id).orElseThrow();
        task.setCompleted(!task.isCompleted());
        return taskRepo.save(task);
    }
}

package com.aws_project.task_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import com.aws_project.task_manager.model.Category;
import com.aws_project.task_manager.model.Task;
import com.aws_project.task_manager.repo.CategoryRepo;
import com.aws_project.task_manager.repo.TaskRepo;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @Bean
    public CommandLineRunner setupDefaultCategory(CategoryRepo categoryRepo) {
        return args -> {
            Category uncategorized = categoryRepo.findByName("Uncategorized")
                    .orElseGet(() -> {
                        Category c = new Category();
                        c.setName("Uncategorized");
                        return categoryRepo.save(c);
                    });

            List<Task> tasksWithoutCategory = taskRepo.findAll()
                    .stream()
                    .filter(t -> t.getCategory() == null)
                    .toList();

            for (Task t : tasksWithoutCategory) {
                t.setCategory(uncategorized);
            }

            taskRepo.saveAll(tasksWithoutCategory);
        };
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task, @RequestParam Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        task.setCategory(category);
        return taskRepo.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask,
            @RequestParam(required = false) Long categoryId) {
        Task task = taskRepo.findById(id).orElseThrow();
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setDueTime(updatedTask.getDueTime());
        task.setPriority(updatedTask.getPriority());
        task.setCompleted(updatedTask.isCompleted());

        Category category;
        if (categoryId != null) {
            category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } else {
            category = categoryRepo.findByName("Uncategorized")
                    .orElseThrow(() -> new RuntimeException("Default category missing"));
        }
        task.setCategory(category);

        return taskRepo.save(task);
    }

    @PutMapping("/tasks/{id}/category")
    public Task updateTaskCategory(@PathVariable Long id, @RequestParam Long categoryId) {
        Task task = taskRepo.findById(id).orElseThrow();
        Category category = categoryRepo.findById(categoryId).orElseThrow();
        task.setCategory(category);
        return taskRepo.save(task);
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepo.deleteById(id);
    }

    @PutMapping("/{id}/toggle")
    public Task toggleCompleted(@PathVariable Long id) {
        Task task = taskRepo.findById(id).orElseThrow();
        task.setCompleted(!task.isCompleted());
        return taskRepo.save(task);
    }
}

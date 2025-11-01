package com.aws_project.task_manager;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.aws_project.task_manager.model.Priority;
import com.aws_project.task_manager.model.Task;
import com.aws_project.task_manager.model.Category;
import com.aws_project.task_manager.repo.TaskRepo;
import com.aws_project.task_manager.repo.CategoryRepo;

@SpringBootApplication
public class TaskManagerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TaskManagerApplication.class, args);

		TaskRepo taskRepo = context.getBean(TaskRepo.class);
		CategoryRepo categoryRepo = context.getBean(CategoryRepo.class);

		Category workCategory = categoryRepo.findByName("Work")
				.orElseGet(() -> {
					Category c = new Category();
					c.setName("Work");
					return categoryRepo.save(c);
				});

		Task task = new Task();
		task.setTitle("Test Task");
		task.setDescription("Checking if the code works");
		task.setDueDate(LocalDate.parse("2025-11-10"));
		task.setDueTime(LocalTime.parse("16:30"));
		task.setPriority(Priority.HIGH);
		task.setCompleted(false);
		task.setCategory(workCategory);

		Task savedTask = taskRepo.save(task);

		System.out.println("Saved Task:");
		System.out.println("ID: " + savedTask.getId());
		System.out.println("Title: " + savedTask.getTitle());
		System.out.println("Description: " + savedTask.getDescription());
		System.out.println("Due Date: " + savedTask.getDueDate());
		System.out.println("Due Time: " + savedTask.getDueTime());
		System.out.println("Priority: " + savedTask.getPriority());
		System.out.println("Completed: " + savedTask.isCompleted());
		System.out.println("Category: " + savedTask.getCategory().getName());
	}
}

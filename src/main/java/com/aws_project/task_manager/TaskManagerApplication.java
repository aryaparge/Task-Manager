package com.aws_project.task_manager;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.aws_project.task_manager.model.Priority;
import com.aws_project.task_manager.model.Task;
import com.aws_project.task_manager.repo.TaskRepo;

@SpringBootApplication
public class TaskManagerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TaskManagerApplication.class, args);

		Task task1 = new Task();
		task1.setTitle("Test task");
		task1.setDescription("testing to see if code works");
		task1.setDueDate(LocalDate.parse("2025-11-10"));
		task1.setDueTime(LocalTime.parse("16:30"));
		task1.setCompleted(false);
		task1.setPriority(Priority.HIGH);

		TaskRepo taskRepo = context.getBean(TaskRepo.class);
		taskRepo.save(task1);

		System.out.println("Task saved successfully!");
		System.out.println("Task Details:");
		System.out.println("Title: " + task1.getTitle());
		System.out.println("Description: " + task1.getDescription());
		System.out.println("Due Date: " + task1.getDueDate());
		System.out.println("Due Time: " + task1.getDueTime());
		System.out.println("Completed: " + task1.isCompleted());
		System.out.println("Priority: " + task1.getPriority());
	}

}

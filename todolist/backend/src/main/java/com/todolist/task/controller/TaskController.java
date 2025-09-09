package com.todolist.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.todolist.task.model.Task;
import com.todolist.task.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins="*")
public class TaskController {

  @Autowired
  private TaskService taskService;
  
  @GetMapping
  public List<Task>getAllTask(){
    return taskService.getAllTasks();
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody Task task){
    if(task.getTitle()==null||task.getTitle().isBlank())
      return ResponseEntity.badRequest().build();
    return ResponseEntity.status(201).body(taskService.CreateTask(task));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task task){
    try{
      return ResponseEntity.ok(taskService.UpdateTask(id, task));      
    }catch (RuntimeException e){
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable int id){
    taskService.DeleteTask(id);
    return ResponseEntity.ok().build();
  }
}


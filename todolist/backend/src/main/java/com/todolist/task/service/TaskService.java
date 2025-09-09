package com.todolist.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.task.model.Task;
import com.todolist.task.repository.TaskRepo;

@Service
public class TaskService {

  @Autowired
  private TaskRepo taskrepo;

  public List<Task>getAllTasks(){
    return taskrepo.findAll();
  }

  public Optional<Task>getTaskById(int id){
    return taskrepo.findById(id);
  }

  public Task CreateTask(Task task){
    return taskrepo.save(task);
  }

  public void DeleteTask(int id){
     taskrepo.deleteById(id);
  }

  public Task UpdateTask(int id,Task task)  {
    return taskrepo.findById(id)
      .map(t->{
        t.setTitle(task.getTitle());
        t.setCompleted(task.getCompleted());
        return taskrepo.save(t);
      }).orElseThrow(()->new RuntimeException("Task not found"));
  }
}


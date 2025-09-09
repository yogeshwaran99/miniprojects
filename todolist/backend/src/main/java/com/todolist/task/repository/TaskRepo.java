package com.todolist.task.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.todolist.task.model.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task,Integer>{

  
}

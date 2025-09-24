package com.todolist.task.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.task.model.Task;

@Service
public class TaskService {

  @Autowired
  private DataSource dataSource;

  private Connection getConnection() throws SQLException{
    return dataSource.getConnection();
  }


  public List<Task>getAllTasks(){

    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT id, title, completed FROM task";
    try(Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet r = st.executeQuery(sql)){
      while(r.next()){
        Task t= new Task();
        t.setId(r.getInt("id"));
        t.setTitle(r.getString("title"));
        t.setCompleted(r.getBoolean("completed"));
        tasks.add(t);
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    return tasks;
  }

  public Task CreateTask(Task task){
    String sql = "INSERT INTO task (title,completed) VALUES (?,?) RETURNING id";
    try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);){

      ps.setString(1,task.getTitle());
      ps.setBoolean(2,task.getCompleted()!=null? task.getCompleted():false);
      ResultSet r = ps.executeQuery();
      if(r.next()){
        task.setId(r.getInt("id"));
      }
    }catch (SQLException e){
      e.printStackTrace();
    }
    return task;
  }

  public void DeleteTask(int id){
    String sql = "DELETE FROM task WHERE id=?";
    try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){

      ps.setInt(1,id);
      ps.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  public Task UpdateTask(int id,Task task)  {

    String sql = "UPDATE task SET title = ?, completed=? WHERE id=?";

    try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
      ps.setString(1,task.getTitle());
      ps.setBoolean(2, task.getCompleted()!=null ? task.getCompleted():false);
      ps.setInt(3,id);

      int row = ps.executeUpdate();
      if(row==0) throw new RuntimeException("Task not found");
      task.setId(id);
    }catch(SQLException e){
      e.printStackTrace();
    }

    return task;
  }
}

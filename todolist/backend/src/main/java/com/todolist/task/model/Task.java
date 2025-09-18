package com.todolist.task.model;

public class Task {

  private int id;
  private String title;
  private Boolean completed=false;

  public Task(){}
  public Task(int id, String title, Boolean completed){
    this.id = id;
    this.title = title;
    this.completed = completed != null ? completed : false;
  }
  public void setId(int id) {
    this.id =id;
  }
  public void  setTitle(String title){
    this.title =title;
  }
  public void setCompleted(Boolean completed){
    this.completed = completed;
  }
  public int getId(){
    return id;
  }
  public String getTitle(){
    return title;
  }
  public Boolean getCompleted(){
    return completed;
  }

}

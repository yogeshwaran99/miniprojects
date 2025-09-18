package com.todolist.task.model;

public class Task {

  int id;
  String title;
  Boolean completed;

 
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

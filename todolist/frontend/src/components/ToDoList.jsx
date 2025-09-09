import React from "react";

const TodoList = ({
  tasks,
  deleteTask,
  updateTask,
  editingTitle,
  setEditingTitle,
  editingTaskId,
  setEditingTaskId,
  startEditing,
  handleEditChange,
}) => {

  return (
    <ul>
      {tasks.map((task) => (
        <li key={task.id}>
          <input
            type="checkbox"
            checked={task.completed}
            onChange={() => updateTask(task.id, {completed: !task.completed})}
          />
          {editingTaskId === task.id ? (
            <>
              <input type="text" value={editingTitle} onChange={handleEditChange} />
              <button
                onClick={() => {
                  updateTask(task.id, {title: editingTitle});
                  setEditingTaskId(null);
                }}
              >
                Save
              </button>
            </>
          ) : (
            <>
              <span style={{textDecoration: task.completed ? "line-through" : "none"}}>
                {task.title}
              </span>

              <div>
                <button onClick={() => deleteTask(task.id)}>Delete</button>
                <button
                  onClick={() => {
                    startEditing(task.id);
                    setEditingTitle(task.title);
                  }}
                >
                  Edit
                </button>
              </div>
            </>
          )}
        </li>
      ))}
    </ul>
  );
};

export default TodoList;

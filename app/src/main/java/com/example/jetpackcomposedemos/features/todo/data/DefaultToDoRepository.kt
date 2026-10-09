package com.example.jetpackcomposedemos.features.todo.data

import com.example.jetpackcomposedemos.features.todo.domain.ToDoRepository
import javax.inject.Inject
import com.example.jetpackcomposedemos.features.todo.domain.ToDo


class DefaultToDoRepository @Inject constructor(

    private val toDoDao: ToDoDao
): ToDoRepository {

    override suspend fun getToDos(): List<ToDo>  {
        return toDoDao.getToDos().map {
            ToDoMapper.toDomain(it)
        }
    }

    override suspend fun getToDo(id:Int): ToDo? {
        // how we would do it with the map
//        val entity = toDoDao.getToDo(id) ?: return null
//        return ToDoMapper.toDomain(entity)

        // map without the loop
        return toDoDao.getToDo(id)?.let {
            ToDoMapper.toDomain(it)
        }
    }

    override suspend fun insert(todo: ToDo){
        toDoDao.insertToDo(ToDoMapper.toEntity(todo))
    }

    override suspend fun update(todo: ToDo){
        toDoDao.updateToDo(ToDoMapper.toEntity(todo))
    }
}

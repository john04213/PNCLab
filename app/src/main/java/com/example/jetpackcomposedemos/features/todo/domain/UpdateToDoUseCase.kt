package com.example.jetpackcomposedemos.features.todo.domain

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpdateToDoUseCase @Inject constructor (
     private val toDoRepository: ToDoRepository
) {
    suspend operator fun invoke(todo: ToDo) {
        toDoRepository.update(todo)
    }
}

package com.example.jetpackcomposedemos.features.todo.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReOrderToDoUseCase @Inject constructor(
    private val toDoRepository: ToDoRepository
) {

    suspend operator fun invoke(orderedIds: List<Int>) {
        toDoRepository.reorder(orderedIds)
    }
}
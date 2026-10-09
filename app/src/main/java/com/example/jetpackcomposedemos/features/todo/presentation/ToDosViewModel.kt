package com.example.jetpackcomposedemos.features.todo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposedemos.core.LoadableState
import com.example.jetpackcomposedemos.features.todo.domain.CreateToDoUseCase
import com.example.jetpackcomposedemos.features.todo.domain.GetToDosUseCase
import com.example.jetpackcomposedemos.features.todo.domain.ToDo
import com.example.jetpackcomposedemos.features.todo.domain.UpdateToDoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ToDosViewModel @Inject constructor (

    private val getToDos: GetToDosUseCase,
    private val createToDo: CreateToDoUseCase,
    private val updateToDo: UpdateToDoUseCase

): ViewModel() {

    private val _uiState: MutableStateFlow<LoadableState<List<ToDo>>> = MutableStateFlow(LoadableState.Loading)

    val uiState: StateFlow<LoadableState<List<ToDo>>> = _uiState.asStateFlow()


    fun loadToDos(){
        viewModelScope.launch {
            _uiState.value = LoadableState.Success(getToDos())
        }
    }

    fun createToDo(title: String) {
        if(title.isNotEmpty()) {
            viewModelScope.launch {
                createToDo(ToDo(
                    id = 0,
                    title = title,
                    completed = false
                ))
                loadToDos()
            }
        }
    }

    fun toggleCompleted(toDo: ToDo) {
        viewModelScope.launch {
            updateToDo(toDo.copy(completed = !toDo.completed))
            loadToDos()
        }
    }
}

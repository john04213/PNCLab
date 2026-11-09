package com.example.jetpackcomposedemos.features.todo.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.jetpackcomposedemos.R
import com.example.jetpackcomposedemos.features.todo.domain.ToDo
import com.example.jetpackcomposedemos.ui.theme.LocalSpacing
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDosList(
    toDos: List<ToDo>,
    useTwoPaneLayout: Boolean,
    onAdd: (String) -> Unit,
    onToggleCompleted: (ToDo) -> Unit,
    onDelete: (ToDo) -> Unit = {},
    onUndoDelete: (ToDo) -> Unit = {},
    onReorder: (List<Int>) -> Unit = {}
) {
    var showDrawer by rememberSaveable() {
        mutableStateOf(false)
    }
    var newText by rememberSaveable() {
        mutableStateOf("")
    }


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val deletedMessage = stringResource(R.string.todo_deleted)
    val undoLabel = stringResource(R.string.todo_undo)


    // a local copy of the to-dos list that the drag can reoroder
    // while the finger is still down
    // a new list from the viewModel will replace it
    val items = remember(toDos) {
        mutableStateListOf<ToDo>().apply {addAll(toDos) }
    }

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        listState = listState,
        onMove = { from, to -> items.add(to, items.removeAt(from))},
        onDragEnd = { onReorder(items.map { it.id })}
    )


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDrawer = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a new to-do item"
                )
            }
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(LocalSpacing.current.large),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
        ) {
            items(items, key = {it.id }) { toDo ->
                val isDragging = toDo.id == dragDropState.draggingItemKey

                // the row being dragged follow the finger;
                // the others slide out of the way
                val rowModifier = if (isDragging) {
                    Modifier
                        .zIndex(1f)
                        .graphicsLayer { translationY = dragDropState.draggingItemOffset }
                } else {
                    Modifier.animateItem()
                }.dragHandle(dragDropState, toDo.id)

                SwipeToDeleteRow(
                    onDelete = {
                        onDelete(toDo)
                        scope.launch {
                            // show only one snackbar at a time
                            snackbarHostState.currentSnackbarData?.dismiss()

                            // offer to take back the deletion
                            val result = snackbarHostState.showSnackbar(
                                message = deletedMessage,
                                actionLabel = undoLabel,
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                onUndoDelete(toDo)
                            }
                        }
                    },
                    modifier = rowModifier,
                ) {
                    ToDoCard(
                        toDo = toDo,
                        onDoubleTap = {
                            onToggleCompleted(toDo)
                        }
                    )
                }
            }
        }

        if (showDrawer) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDrawer = false
                }
            ) {
                TextField(
                    value = newText,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onValueChange = { newText = it },
                    label = { Text(stringResource(R.string.todo_new_hint)) }
                )

                Button(
                    onClick = {
                        onAdd(newText)
                        showDrawer = false
                        newText = ""
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(stringResource(R.string.todo_add_item))
                }
            }
        }
    }
}


@Composable
private fun SwipeToDeleteRow(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { target ->
            if (target == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
            true
        }
    )

    // rememberSwipeToDismissBoxState is built on rememberSaveablel, and the list
    // keeps saved state for a row that was just removed
    // if undo brings the row back before that state  is dropped
    // it would reappear still swiped awat
    // a fresh row is always "settled"
    LaunchedEffect(Unit) {
        if(dismissState.currentValue != SwipeToDismissBoxValue.Settled)  {
            dismissState.reset()
        }
    }
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(20.dp),
                contentAlignment = Alignment.CenterEnd

            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.todo_delete),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        content()
    }
}
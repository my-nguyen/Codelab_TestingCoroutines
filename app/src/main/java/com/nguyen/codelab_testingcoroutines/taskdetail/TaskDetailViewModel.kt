package com.nguyen.codelab_testingcoroutines.taskdetail

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.nguyen.codelab_testingcoroutines.Event
import com.nguyen.codelab_testingcoroutines.R
import com.nguyen.codelab_testingcoroutines.data.Result
import com.nguyen.codelab_testingcoroutines.data.Task
import com.nguyen.codelab_testingcoroutines.data.source.TasksRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Details screen.
 */
class TaskDetailViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    private val _taskId = MutableLiveData<String>()

    private val _task = _taskId.switchMap { taskId ->
        tasksRepository.observeTask(taskId).map { computeResult(it) }
    }
    val task: LiveData<Task?> = _task

    val isDataAvailable: LiveData<Boolean> = _task.map { it != null }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _editTaskEvent = MutableLiveData<Event<Unit>>()
    val editTaskEvent: LiveData<Event<Unit>> = _editTaskEvent

    private val _deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val deleteTaskEvent: LiveData<Event<Unit>> = _deleteTaskEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    // This LiveData depends on another so we can use a transformation.
    val completed: LiveData<Boolean> = _task.map { input: Task? ->
        input?.isCompleted ?: false
    }

    fun deleteTask() = viewModelScope.launch {
        _taskId.value?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskEvent.value = Event(Unit)
        }
    }

    fun editTask() {
        _editTaskEvent.value = Event(Unit)
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = _task.value ?: return@launch
        if (completed) {
            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun start(taskId: String) {
        // If we're already loading or already loaded, return (might be a config change)
        if (_dataLoading.value == true || taskId == _taskId.value) {
            return
        }
        // Trigger the load
        _taskId.value = taskId
    }

    private fun computeResult(taskResult: Result<Task>): Task? {
        return if (taskResult is Result.Success) {
            taskResult.data
        } else {
            showSnackbarMessage(R.string.loading_tasks_error)
            null
        }
    }


    fun refresh() {
        // Refresh the repository and the task will be updated automatically.
        _task.value?.let {
            _dataLoading.value = true
            viewModelScope.launch {
                tasksRepository.refreshTask(it.id)
                _dataLoading.value = false
            }
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }
}

@Suppress("UNCHECKED_CAST")
class TaskDetailViewModelFactory(private val tasksRepository: TasksRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = (TaskDetailViewModel(tasksRepository) as T)
}
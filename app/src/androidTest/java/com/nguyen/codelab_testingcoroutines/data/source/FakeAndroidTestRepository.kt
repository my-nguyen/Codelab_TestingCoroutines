package com.nguyen.codelab_testingcoroutines.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nguyen.codelab_testingcoroutines.data.Result
import com.nguyen.codelab_testingcoroutines.data.Task
import kotlinx.coroutines.test.runTest

class FakeAndroidTestRepository : TasksRepository {

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private var shouldReturnError = false

    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runTest { refreshTasks() }
        return observableTasks
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runTest { refreshTasks() }
        return observableTasks.map { tasks ->
            when (tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
            }
        }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        tasksServiceData[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task"))
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun saveTask(task: Task) {
        tasksServiceData[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, true, task.id)
        tasksServiceData[task.id] = completedTask
    }

    override suspend fun completeTask(taskId: String) {
        // Not required for the remote data source.
        throw NotImplementedError()
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, false, task.id)
        tasksServiceData[task.id] = activeTask
    }

    override suspend fun activateTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun clearCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteTask(taskId: String) {
        tasksServiceData.remove(taskId)
        refreshTasks()
    }

    override suspend fun deleteAllTasks() {
        tasksServiceData.clear()
        refreshTasks()
    }


    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            tasksServiceData[task.id] = task
        }
        runTest { refreshTasks() }
    }
}

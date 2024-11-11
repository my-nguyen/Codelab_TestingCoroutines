package com.nguyen.codelab_testdoubles

import android.content.Context
import androidx.room.Room
import com.nguyen.codelab_testdoubles.data.source.DefaultTasksRepository
import com.nguyen.codelab_testdoubles.data.source.TasksDataSource
import com.nguyen.codelab_testdoubles.data.source.TasksRepository
import com.nguyen.codelab_testdoubles.data.source.local.TasksLocalDataSource
import com.nguyen.codelab_testdoubles.data.source.local.ToDoDatabase
import com.nguyen.codelab_testdoubles.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.VisibleForTesting

object ServiceLocator {
    private var database: ToDoDatabase? = null

    @Volatile
    var tasksRepository: TasksRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    fun provideTasksRepository(context: Context): TasksRepository {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
        val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDataBase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(context.applicationContext, ToDoDatabase::class.java, "Tasks.db").build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                TasksRemoteDataSource.deleteAllTasks()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }
}
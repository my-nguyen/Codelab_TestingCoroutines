package com.nguyen.codelab_testingbasics.tasks

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

// @Config(sdk = [Build.VERSION_CODES.R])
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @Test
    fun addNewTask_setsNewTaskEvent() {
        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        tasksViewModel.addNewTask()

        // Then the new task event is triggered
    }
}
package com.nguyen.codelab_testdoubles.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.nguyen.codelab_testdoubles.R
import com.nguyen.codelab_testdoubles.data.Task
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest {
    @Test
    fun activeTaskDetails_DisplayedInUi() {
        val activeTask = Task("Active Task", "AndroidX Rocks", false)

        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.Theme_Codelab_TestDoubles)
    }
}
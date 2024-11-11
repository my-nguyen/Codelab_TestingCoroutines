package com.nguyen.codelab_testingcoroutines.data.source

import com.nguyen.codelab_testingcoroutines.MainCoroutineRule
import com.nguyen.codelab_testingcoroutines.data.Result
import com.nguyen.codelab_testingcoroutines.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest {
    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description2")
    private val task3 = Task("Title3", "Description3")
    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource
    private lateinit var tasksRepository: DefaultTasksRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        tasksRepository = DefaultTasksRepository(tasksRemoteDataSource, tasksLocalDataSource, Dispatchers.Main)
    }

    @Test
    // can't change it to mainCoroutineRule.runTest because MainCoroutineRule can't find a base
    // class that satisfies this: "TestCoroutineScope by TestCoroutineScope(dispatcher)"
    fun getTasks_requestsAllTasksFromRemoteDataSource() = mainCoroutineRule.runTest {
        // When tasks are requested from the tasks repository
        val tasks = tasksRepository.getTasks(true) as Result.Success

        // Then tasks are loaded from the remote data source
        assertThat(tasks.data, IsEqual(remoteTasks))
    }
}
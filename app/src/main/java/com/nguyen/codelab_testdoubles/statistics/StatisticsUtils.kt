package com.nguyen.codelab_testdoubles.statistics

import com.nguyen.codelab_testdoubles.data.Task

/**
 * Function that does some trivial computation. Used to showcase unit tests.
 */
internal fun getActiveAndCompletedStats(tasks: List<Task>?): StatsResult {
    if (tasks.isNullOrEmpty())
        return StatsResult(0f, 0f)

    val totalTasks = tasks.size
    val numberOfActiveTasks = tasks.count { it.isActive }
    val active = 100f * numberOfActiveTasks / totalTasks
    val completed = 100f * (totalTasks - numberOfActiveTasks) / totalTasks
    return StatsResult(active, completed)
}

data class StatsResult(val activeTasksPercent: Float, val completedTasksPercent: Float)

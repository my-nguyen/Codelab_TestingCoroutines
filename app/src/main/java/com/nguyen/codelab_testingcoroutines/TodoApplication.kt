package com.nguyen.codelab_testingcoroutines

import android.app.Application
import com.nguyen.codelab_testingcoroutines.data.source.TasksRepository
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 *
 * Also, sets up Timber in the DEBUG BuildConfig. Read Timber's documentation for production setups.
 */
class TodoApplication : Application() {
    val taskRepository: TasksRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())
    }
}

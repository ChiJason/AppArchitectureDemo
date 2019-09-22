package android.chi.jason.cleanarchitecturedemo.ui

import android.chi.jason.cleanarchitecturedemo.MainActivity
import android.chi.jason.cleanarchitecturedemo.R
import android.chi.jason.cleanarchitecturedemo.ViewMatcherUtil
import android.chi.jason.cleanarchitecturedemo.ViewModelUtil
import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.intercepting.SingleActivityFactory
import com.nhaarman.mockitokotlin2.*
import kotlinx.android.synthetic.main.fragment_task_detail.*
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val activityFactory = object : SingleActivityFactory<MainActivity>(MainActivity::class.java) {
        override fun create(intent: Intent?) = MainActivity().apply {
            viewModel.stub {
                on { taskList }.thenReturn(taskList)
                on { selectedTask }.thenReturn(selectedTask)
                on { dueDate }.thenReturn(dueDate)
            }
            doNothing().whenever(viewModel).loadTasks()
            doNothing().whenever(viewModel).loadTasksSortByDueDate()
            supportFragmentManager.registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            when (f) {
                                is TaskFragment -> f.viewModelFactory = ViewModelUtil.createFor(viewModel)
                                is AddTaskFragment -> f.viewModelFactory = ViewModelUtil.createFor(viewModel)
                                is TaskDetailFragment -> f.viewModelFactory = ViewModelUtil.createFor(viewModel)
                            }
                        }
                    }, true
            )
        }
    }

    @get:Rule
    val activityTestRule = ActivityTestRule(activityFactory, true, true)

    @get:Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    private val viewModel: TaskViewModel = mock()
    private val taskList = MutableLiveData<MutableList<TaskEntity>>()
    private val selectedTask = MutableLiveData<TaskEntity>()
    private val dueDate = MutableLiveData<Long>()

    @Before
    fun init() {

        val list = mutableListOf<TaskEntity>().apply {
            add(TaskEntity(0, "desc", 1, 0))
            for (i in 1 until 11) {
                add(TaskEntity(i, "desc", 0, 0))
            }
        }
        taskList.postValue(list)
    }

    @Test
    fun should_ShowTaskFragment_When_MainActivityIsOpen() {

        verify(viewModel).loadTasks()
        onView(withId(R.id.taskRv)).check(matches(isDisplayed()))
        onView(withId(R.id.taskRv)).check(matches(ViewMatcherUtil.recyclerViewSizeMatcher(11)))
        onView(ViewMatcherUtil.viewHolderMatcher(R.id.taskRv, 0, R.id.checkbox)).check(matches(isChecked()))
        onView(withId(R.id.addTaskFab)).check(matches(isDisplayed()))

        //assert menu
        openActionBarOverflowOrOptionsMenu(
                ApplicationProvider.getApplicationContext<Context>()
        )
        onView(withText("Settings"))
    }

    @Test
    fun should_ShowAddTaskFragment_When_FabOnTaskFragmentIsClicked() {

        onView(withId(R.id.addTaskFab)).perform(click())
        onView(withId(R.id.action_save)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_description)).check(matches(withHint(R.string.task_description)))
        onView(withId(R.id.switch_priority))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.task_priority)))
        onView(withId(R.id.select_date)).check(matches(isDisplayed()))
    }

    @Test
    fun should_InsertTask_When_UserAddedNewTask() {

        //Set AddTaskFragment
        activityTestRule.activity.setFragment(AddTaskFragment.newInstance())
        onView(withId(R.id.action_save)).perform(click())
        val date = System.currentTimeMillis()
        dueDate.postValue(date)
        onView(withId(R.id.text_date)).check(matches(withText(DateUtils.getRelativeTimeSpanString(activityTestRule.activity, date).toString())))

        //show toast if description is empty
        onView(withText("description can't be blank"))
                .inRoot(RootMatchers.withDecorView(Matchers.not(activityTestRule.activity.window.decorView)))
                .check(matches(isDisplayed()))

        doNothing().whenever(viewModel).addTask(any())
        onView(withId(R.id.text_input_description)).perform(typeText("write test"))
        onView(withId(R.id.action_save)).perform(click())

        verify(viewModel).addTask(any())
    }

    @Test
    fun should_ShowTaskDetailFragment_When_TaskIsSelected() {

        onView(withId(R.id.taskRv)).perform(actionOnItemAtPosition<TaskAdapter.TaskViewHolder>(0, click()))
        onView(withId(R.id.action_delete)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_description)).check(matches(withText("desc")))
        onView(withId(R.id.detail_dueDate)).check(matches(withText(R.string.date_empty)))
    }

    @Test
    fun should_DeleteTaskFromDb_When_UserClickDeleteBtn() {

        selectedTask.postValue(TaskEntity(99, "test", 1, 0))
        //open detail fragment
        activityTestRule.activity.setFragment(TaskDetailFragment.newInstance())
        onView(withId(R.id.detail_description)).check(matches(withText("test")))

        doNothing().whenever(viewModel).deleteTask(any())
        onView(withId(R.id.action_delete)).perform(click())

        verify(viewModel).deleteTask(99)
    }

}
package android.chi.jason.cleanarchitecturedemo

import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import android.chi.jason.cleanarchitecturedemo.repository.TaskRepository
import android.chi.jason.cleanarchitecturedemo.ui.TaskViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt

class TaskViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    private val taskList: Observer<MutableList<TaskEntity>> = mock()
    private val selectedTask: Observer<TaskEntity> = mock()
    private val dueDate: Observer<Long> = mock()
    private val taskRepo: TaskRepository = mock()
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        viewModel = TaskViewModel(taskRepo)
        viewModel.taskList.observeForever(taskList)
        viewModel.selectedTask.observeForever(selectedTask)
        viewModel.dueDate.observeForever(dueDate)
    }

    @Test
    fun `Should GetAllTasks When LoadTasksIsCalled`() {

        //fake data
        val list = mutableListOf<TaskEntity>().apply {
            for (i in 0 until 10) {
                add(TaskEntity(i, "desc$i", 0, 0))
            }
        }
        whenever(taskRepo.getAllTasks()).thenReturn(Single.just(list))

        viewModel.loadTasks()

        verify(taskList).onChanged(any())
        assertEquals(10, viewModel.taskList.value!!.size)
    }

    @Test
    fun `Should GetSortedTasks When LoadTasksSortByDueDateCalled`() {

        //fake data
        val list = mutableListOf<TaskEntity>().apply {
            for (i in 0 until 10) {
                add(TaskEntity(i, "desc$i", 0, 0))
            }
        }
        whenever(taskRepo.getAllTasksSortByDueDate()).thenReturn(Single.just(list))

        viewModel.loadTasksSortByDueDate()

        verify(taskList).onChanged(any())
        assertEquals(10, viewModel.taskList.value!!.size)
    }

    @Test
    fun `Should AddTaskToDB When UserAddNewTask`() {

        doNothing().whenever(taskRepo).addTask(any())

        val taskEntity = TaskEntity(10, "desc", 0, 1)

        viewModel.addTask(taskEntity)

        argumentCaptor<TaskEntity> {
            verify(taskRepo).addTask(capture())
            assertEquals(taskEntity, firstValue)
        }
    }

    @Test
    fun `Should UpdateCompletionStatus When UserCheckTheCheckBox`() {

        doNothing().whenever(taskRepo).updateIsCompleteById(anyBoolean(), anyInt())

        viewModel.updateTaskCompletion(true, 1)

        verify(taskRepo).updateIsCompleteById(true, 1)
    }

    @Test
    fun `Should DeleteTaskFromDb When UserClickDelBtn`() {

        doNothing().whenever(taskRepo).deleteTask(anyInt())

        viewModel.deleteTask(7)

        argumentCaptor<Int> {
            verify(taskRepo).deleteTask(capture())
            assertEquals(7, firstValue)
        }
    }
}
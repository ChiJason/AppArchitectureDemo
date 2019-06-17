package android.chi.jason.cleanarchitecturedemo.repository

import android.chi.jason.cleanarchitecturedemo.db.TaskDao
import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import io.reactivex.Single
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao) {

    fun addTask(vararg taskEntity: TaskEntity) {
        taskDao.insertTask(*taskEntity)
    }

    fun getAllTasks(): Single<MutableList<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    fun getAllTasksSortByDueDate(): Single<MutableList<TaskEntity>> {
        return taskDao.getAllTasksSortByDueDate()
    }

    fun updateIsCompleteById(isComplete: Boolean, id: Int) {
        taskDao.updateIsCompleteById(if (isComplete) 1 else 0, id)
    }

    fun deleteTask(id: Int) {
        taskDao.deleteTaskById(id)
    }
}
package android.chi.jason.cleanarchitecturedemo.ui

import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import android.chi.jason.cleanarchitecturedemo.repository.TaskRepository
import android.chi.jason.cleanarchitecturedemo.testing.Mockable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Mockable
class TaskViewModel @Inject constructor(
        private val taskRepo: TaskRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val taskList = MutableLiveData<MutableList<TaskEntity>>()
    val selectedTask = MutableLiveData<TaskEntity>()
    val dueDate = MutableLiveData<Long>()

    fun loadTasks() {
        taskRepo.getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            taskList.value = it
                        }
                ).addTo(disposable)
    }

    fun loadTasksSortByDueDate() {
        taskRepo.getAllTasksSortByDueDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            taskList.value = it
                        }
                ).addTo(disposable)
    }

    fun addTask(vararg taskEntity: TaskEntity) {
        Completable.fromAction { taskRepo.addTask(*taskEntity) }
                .subscribeOn(Schedulers.io())
                .subscribe()
                .addTo(disposable)
    }

    fun updateTaskCompletion(isComplete: Boolean, id: Int) {
        Completable.fromAction { taskRepo.updateIsCompleteById(isComplete, id) }
                .subscribeOn(Schedulers.io())
                .subscribe()
                .addTo(disposable)
    }

    fun deleteTask(id: Int) {
        Completable.fromAction { taskRepo.deleteTask(id) }
                .subscribeOn(Schedulers.io())
                .subscribe()
                .addTo(disposable)
    }

}
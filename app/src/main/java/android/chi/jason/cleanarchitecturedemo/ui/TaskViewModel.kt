package android.chi.jason.cleanarchitecturedemo.ui

import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import android.chi.jason.cleanarchitecturedemo.repository.TaskRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val taskRepo: TaskRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val taskList = MutableLiveData<MutableList<TaskEntity>>()
    val selectedTask = MutableLiveData<TaskEntity>()

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

}
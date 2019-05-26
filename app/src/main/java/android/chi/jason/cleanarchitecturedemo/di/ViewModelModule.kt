package android.chi.jason.cleanarchitecturedemo.di

import android.chi.jason.cleanarchitecturedemo.ui.TaskViewModel
import android.chi.jason.cleanarchitecturedemo.viewmodel.ViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    abstract fun bindTaskViewModel(taskViewModel: TaskViewModel): ViewModel
}
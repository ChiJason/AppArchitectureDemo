package android.chi.jason.cleanarchitecturedemo.di

import android.chi.jason.cleanarchitecturedemo.ui.TaskFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeTaskFragment(): TaskFragment

}
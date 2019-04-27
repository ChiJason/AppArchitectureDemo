package android.chi.jason.cleanarchitecturedemo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(vararg task: TaskEntity)

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): Single<MutableList<TaskEntity>>

    @Query("UPDATE task_table SET is_complete = :isComplete WHERE id = :id")
    fun updateIsCompleteById(isComplete: Int, id: Int)

    @Query("UPDATE task_table SET is_priority = :isPriority WHERE id = :id")
    fun updateIsPriorityById(isPriority: Int, id: Int)

    @Query("DELETE FROM task_table WHERE id = :id")
    fun deleteTaskById(id: Int)
}
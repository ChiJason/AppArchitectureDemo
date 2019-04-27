package android.chi.jason.cleanarchitecturedemo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TaskEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "is_complete") val isComplete: Int,
        @ColumnInfo(name = "is_priority") val isPriority: Int,
        @ColumnInfo(name = "due_date") val dueDateMills: Long = Long.MAX_VALUE)
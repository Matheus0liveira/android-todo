package com.matheus0liveira.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "value") var value: String,
    @ColumnInfo(name = "is_check") var isCheck: Boolean = false,
    @ColumnInfo(name = "is_removed") var isRemoved: Boolean = false,
    @ColumnInfo(name = "created_at") var createdAt: Date = Date()
)
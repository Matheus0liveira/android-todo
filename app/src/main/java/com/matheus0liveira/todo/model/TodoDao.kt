package com.matheus0liveira.todo.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun getAll(): List<Todo>

    @Insert
    fun create(todo: Todo): Long

    @Update
    fun update(todo: Todo)
}
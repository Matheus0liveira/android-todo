package com.matheus0liveira.todo

import android.app.Application
import com.matheus0liveira.todo.model.AppDatabase

class App : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        db = AppDatabase.getDatabase(this)
    }
}
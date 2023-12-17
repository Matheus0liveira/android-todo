package com.matheus0liveira.todo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matheus0liveira.todo.model.Todo
import com.matheus0liveira.todo.model.TodoDao

class ListTodos : AppCompatActivity() {
    private lateinit var todoDao: TodoDao
    private lateinit var todoItems: MutableList<Todo>
    private lateinit var adapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        todoDao = (application as App).db.todoDao()
        setTitle(R.string.list_todos)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_todos)

        todoItems = mutableListOf()
        adapter = MainAdapter(todoItems)

        Thread {
            val result = todoDao.getAll().filter { it.isRemoved }

            runOnUiThread {
                todoItems.addAll(result)
                adapter.notifyDataSetChanged()
            }

        }.start()

        val rv = findViewById<RecyclerView>(R.id.rv_all_list_todo)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)


    }


    private inner class MainAdapter(private val mainItems: List<Todo>) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)

            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.bind(mainItems[position])
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }


        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: Todo) {

                val textView = itemView as TextView

                textView.text = item.getFullValue()

            }

        }

    }
}
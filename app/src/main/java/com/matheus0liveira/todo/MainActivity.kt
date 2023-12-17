package com.matheus0liveira.todo

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matheus0liveira.todo.model.Todo
import com.matheus0liveira.todo.model.TodoDao


class MainActivity : AppCompatActivity() {

    private lateinit var todoItems: MutableList<Todo>
    private lateinit var adapter: MainAdapter
    private lateinit var todoDao: TodoDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoDao = (application as App).db.todoDao()

        setContentView(R.layout.activity_main)

        todoItems = mutableListOf()
        adapter = MainAdapter(todoItems)

        val rv = findViewById<RecyclerView>(R.id.rv_list_todo)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        val saveBtn = findViewById<Button>(R.id.todo_save_btn)
        val todoText = findViewById<EditText>(R.id.todo_text)

        saveBtn.setOnClickListener {

            if (todoText.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Preencha com alguma atividade",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            Thread {

                val todo = Todo(value = todoText.text.toString())
                val id = todoDao.create(todo)
                todo.id = id.toInt()
                runOnUiThread {
                    todoItems.add(todo)

                    adapter.notifyItemInserted(todoItems.size + 1)
                    todoText.text.clear()
                }


            }.start()


        }

        Thread {

            val allTodos = todoDao.getAll().filter { !it.isRemoved }

            runOnUiThread {
                todoItems.addAll(allTodos)
                adapter.notifyDataSetChanged()
            }

        }.start()

    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.show_all_items -> {
                startActivity(Intent(this, ListTodos::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private inner class MainAdapter(private val mainItems: List<Todo>) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.todo_item, parent, false)

            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.bind(mainItems[position], position)
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }


        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: Todo, position: Int) {

                val checkbox = itemView.findViewById<CheckBox>(R.id.todo_item_checkbox)
                val trashBtn = itemView.findViewById<ImageButton>(R.id.todo_item_btn)

                checkbox.isChecked = item.isCheck
                checkbox.text = item.value

                checkbox.setOnClickListener { mainItems[position].isCheck = !item.isCheck }
                trashBtn.setOnClickListener {

                    Thread {
                        item.isRemoved = true
                        todoDao.update(item)

                        runOnUiThread {
                            todoItems.removeAt(position)
                            adapter.notifyDataSetChanged()
                        }

                    }.start()
                }
            }

        }

    }

}

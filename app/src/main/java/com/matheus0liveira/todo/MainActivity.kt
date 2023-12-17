package com.matheus0liveira.todo

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var todoItems: MutableList<Todo>
    private lateinit var adapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoItems = mutableListOf()
        todoItems.add(
            Todo(
                id = 1,
                value = "Make a coffe"
            )
        )
        todoItems.add(
            Todo(
                id = 2,
                value = "Make a Tea"
            )
        )
        todoItems.add(
            Todo(
                id = 3,
                value = "Make a Tea 1"
            )
        )
        todoItems.add(
            Todo(
                id = 4,
                value = "Make a Tea 2"
            )
        )
        todoItems.add(
            Todo(
                id = 5,
                value = "Make a Tea 3"
            )
        )


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

            todoItems.add(
                Todo(
                    id = todoItems.size + 1,
                    value = todoText.text.toString()
                )
            )
            adapter.notifyItemInserted(todoItems.size + 1)
            todoText.text.clear()
        }

    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.show_all_items -> {
                Log.i("CLICKED", "true")
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
                val todoItemValue = itemView.findViewById<EditText>(R.id.todo_item_value)

                todoItemValue.setText(item.getValue())
                checkbox.isChecked = item.getIsCheck()

                todoItemValue.addTextChangedListener {

                    Log.i("OUXII", "$it")
                    mainItems[position].setValue(it.toString())
                }





                checkbox.setOnClickListener {
                    mainItems[position].setIsCheck(!item.getIsCheck())
                    adapter.notifyItemChanged(position)
                }

                trashBtn.setOnClickListener {
                    todoItems.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }

        }

    }

}
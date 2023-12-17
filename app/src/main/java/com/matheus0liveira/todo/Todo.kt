package com.matheus0liveira.todo

data class Todo(
    private var id: Int,
    private var value: String,
    private var isCheck: Boolean = false
) {


    fun setValue(newValue: String) {
        value = newValue
    }

    fun setIsCheck(newIsCheck: Boolean) {
        isCheck = newIsCheck
    }

    fun getValue(): String {

        return value
    }

    fun getIsCheck(): Boolean {

        return isCheck
    }
}

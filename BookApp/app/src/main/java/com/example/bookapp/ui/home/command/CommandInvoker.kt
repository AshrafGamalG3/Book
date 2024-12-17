package com.example.bookapp.ui.home.command

class CommandInvoker {

    private val commands = mutableListOf<Command>()

    fun addCommand(command: Command) {
        commands.add(command)
    }

    fun executeCommands() {
        commands.forEach { it.execute() }
        commands.clear()
    }
}
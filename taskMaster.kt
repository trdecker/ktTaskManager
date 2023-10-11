import java.util.Random
import java.io.File
import java.io.InputStream

class Task {
    public var name: String = ""
    var description: String = ""
    var complete: Boolean = false   
    public var date: Int = 0
    var id: Int = 0

    fun markComplete() {
        complete = true
    }

    fun markIncomplete() {
        complete = false
    }
}

fun main() {
    var tasks = mutableMapOf<String, Task>()
    getStrings()
    return

    // ##### THE LOOP #####
    var input: String = ""
    while (input != "E") {
        clearConsole()
        // Print out the tasks
        printTasks(tasks)
        // Options
        println("Options")
        input = userInput(" * Create new task (A)\n * Edit task (B)\n * Delete task (C)\n * Complete task (D)\n * Exit (E)")
        
        // Create new task
        if (input == "A") {
            val newTask = createTask()
            if (newTask.name != "") {
                tasks.put(newTask.name, newTask)
            }
        }
        // Edit task
        else if (input == "B") {
            if (tasks.size == 0) {
                println("No tasks to edit!")
                Thread.sleep(1000)
                continue
            }

            while (true) {
                var task = chooseTask(tasks)
                if (task.name == "NULL")
                    break
    
                var option: String
                while (true) {
                    clearConsole()
                    printTask(task)
                    option = userInput("Options:\n * Change name (A)\n * Change description (B)\n * Exit (E)")
                    if (option == "A") {
                        val newName = userInput("Enter new name:")
                        tasks.remove(task.name)
                        task.name = newName
                        tasks.put(task.name, task)
                    }
                    else if (option == "B") {
                        val newDesc = userInput("Enter new description:")
                        tasks.remove(task.name)
                        task.description = newDesc
                        tasks.put(task.name, task)
                    }
                    else if (option == "E") {
                        break
                    }
                    else {
                        badInput()
                    }
                }
            }
        }
        // Delete task
        else if (input == "C") {
            if (tasks.size == 0) {
                println("No tasks to delete!")
                Thread.sleep(1000)
                continue
            }

            var task = chooseTask(tasks)
            if (task.name == "NULL")
                continue

            val answer = userInput("Are you sure you want to delete this task? (Y/N)")
            if (answer == "Y") {
                tasks.remove(task.name)
            }
        }
        // Complete task
        else if (input == "D") {
            if (tasks.size == 0) {
                println("No tasks to complete!")
                Thread.sleep(1000)
                continue
            }

            var task = chooseTask(tasks)
            if (task.name == "NULL")
                continue
            
            task.complete = true
        }
        else if (input != "E") {
            badInput()
        }
        clearConsole()
    }

    println("Goodbye!")

}

fun badInput() {
    println("  Incorrect input.")
    Thread.sleep(1000)
}

fun printTask(task: Task) {
    println("### " + task.name + " ###")
    var desc = "     - " + task.description
    if (task.complete)
        desc += " [X]"
    else 
        desc += " [ ]"
    println(desc)
}

fun printTasks(tasks: MutableMap<String, Task>) {
    println("#### My Tasks #####")
    if (tasks.size == 0) {
        println(" * No tasks!")
    }
    else {
        // Print map
        for ((name, task) in tasks) {
            println(" * " + name)
            var desc = "     - " + task.description
            if (task.complete)
                desc += " [X]"
            else 
                desc += " [ ]"
            println(desc)
        }
    }
}

fun chooseTask(tasks: MutableMap<String, Task>): Task {
    while (true) {
        clearConsole()
        printTasks(tasks)
        var taskName = userInput("Which task do you want to edit? (Enter EXIT to leave.)")
        if (taskName == "EXIT" || taskName == "NULL") {
            clearConsole()
            break
        }
            
        val task: Task? = tasks[taskName]
        if (task == null) {
            println("Not found.")
            Thread.sleep(1000)
        }
        else  {
            return task
        }

    }
    var emptyTask = Task()
    emptyTask.name = "NULL"
    return emptyTask
}

fun userInput(prompt: String): String {
    println(prompt)
    print(">>> ")
    var input = readln()
    return input
}

fun createTask(): Task {
    clearConsole()
    var exit: Boolean = false
    var checking: Boolean
    var newTask: Task = Task()
    val emptyTask: Task = Task()
    var input: String
    while (!exit) {
        newTask.name = userInput("Enter name:")
        while (newTask.name == "EXIT") {
            newTask.name = userInput("Sorry, invalid name. Please re-enter.")
        }
        newTask.description = userInput("Create description:")
        // println("Set Date:")
        checking = true
        while (checking) {
            input = userInput("Save? (Y/N)")
            if (input == "Y") {
                return newTask
            }
            if (input == "N") {
                checking = false
                exit = true
            }
            else {
                badInput()
            }
        }   
    }
    return emptyTask
}

fun clearConsole() {
    try {
        val os = System.getProperty("os.name").lowercase()

        if (os.contains("win")) {
            // For Windows
            ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        } else {
            // For Unix-like systems (Linux, macOS)
            Runtime.getRuntime().exec("clear")
        }
    } catch (e: Exception) {
        // Handle exceptions if any
        e.printStackTrace()
    }
}
/**
 * A basic task manager program written in Kotlin.
 * 
 * TODO: Save tasks in .CSV file (or MongoDB)
 * TODO: Debugging
 * TODO: Add date/time implementation
 * TODO: After deleting or completing a task, return to the respective menu, NOT the main menu.
 * FIXME: There's a weird pattern where if the user decides NOT to create a task, an empty task with the title
 *       "NULL" or "EXIT" is created. Modify so this is not the case.
 * FIXME: Stop user from having duplicate titles?
 * FIXME: 
 */

import java.util.Random
import java.io.File
import java.io.InputStream

/**
 * This is the class for the tasks we will be creating and editing.
 * 
 * @property name the title of the task
 * @property description the description of the task
 * @property complete a bool value, completion status
 */
class Task {
    public var name: String = ""
    var description: String = ""
    var complete: Boolean = false   
}

/**
 * The main loop for the function. Initializes 'tasks' list and displays the main menu and tasks.
 */
fun main() {
    var tasks = mutableMapOf<String, Task>()

    // ##### THE LOOP #####
    var input: String = ""
    while (input != "E") {
        clearConsole()
        // Print out the tasks
        printTasks(tasks)
        // Options
        println("Options")
        input = userInput(" * Create new task (A)\n * Edit task (B)\n * Delete task (C)\n * Complete task (D)\n * Exit (E)")
        
        // CREATE TASK
        if (input == "A") {
            val newTask = createTask()
            if (newTask.name != "") {
                tasks.put(newTask.name, newTask)
            }
        }
        // EDIT TASK
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
        // DELETE TASK
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
        // COMPLETE TASK
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

// ##### MENU FUNCTIONS #####
// Functions that ask for user input

/**
 * Prompt the user for a reply.
 * 
 * @param prompt the prompt to request input
 * @return the user input
 */
fun userInput(prompt: String): String {
    println(prompt)
    print(">>> ")
    var input = readln()
    return input
}

/**
 * Display the menu to create a task.
 * 
 * @return the created task, OR an empty task.
 */
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

/**
 * Displays the menu that will allow the user to choose a task.
 * 
 * @param tasks the list of tasks, a map
 * @return the chosenTask, OR an empty task.
 */
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

// ##### OUTPUT FUNCTIONS #####
// Functions that either display something without asking for input

/**
 * Default output for incorrect input.
 */
fun badInput() {
    println("  Incorrect input.")
    Thread.sleep(1000)
}

/**
 * Print the task. Name, description, then a checkbox "[ ]" for completion.
 * 
 * @param task the task being printed
 */
fun printTask(task: Task) {
    println("### " + task.name + " ###")
    var desc = "     - " + task.description
    if (task.complete)
        desc += " [X]"
    else 
        desc += " [ ]"
    println(desc)
}

/**
 * Print ALL the tasks, using the same pattern as printTask.
 * 
 * @param tasks the list of tasks, a MutableMap.
 */
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

/**
 * Clear the console.
 */
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

import jdk.jfr.Description

case class Task(id:Int, description: String, priority:String, status:String)

object TaskManager {
  var tasks = Map[Int, Task]()
  var nextId = 1
  def addTask(description:String, priority:String, status:String): Unit = {
    val task = Task(nextId, description, priority, status)
    task += (nextId -> task)
    nextId += 1
  }
  def removeTask(id:Int):Unit = {
    task -= id
  }

  def updateTask(id:Int, description: Option[String] = None, priority: Option[String] = None, status:Option[String] = None): Unit = {
    tasks.get(id).foreach { task =>
      tasks +=  (id -> task.copy())

    }
  }
}

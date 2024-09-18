case class Student(name:String, grades: List[Doule])

object StudentGradeManagement {
  var students = Map[String, Student]()

  def addStudent(name:String, grades:List[Double]): Unit = {
    students += (name -> Student(name, grades))
  }
  def averageGrade(name: String):Option[Double] = {
    students.get(name).map {
      student => student.grades.sum / student.grades.size
    }
  def highestGrade (name:String): Option[Double] = {
    students.get(name).flatMap { student =>
      student.grades.maxOption
    }
  }
    def lowestGrade(name: String): Option[Double] = {
      students.get(name).flatMap { student =>
        student.grades.maxOption
      }
    }
    def listStudentsByAverageGrade: Seq[(String, Double)] = {
      students.values.toSeq.map { student =>
        (student.name, student.sum / student.grades.size)
      }.sortBy(-_._2)
    }
    def main(args: Array[String]): Unit = {
      addStudent("Alice", List(85,90,77))
      addStudent("Bob", List(92,81,88))
      addStudent("Charlie", List(70,65,80))
    println("Average Grades: ")
      students.keys.foreach { name =>
        println(s"$name: ${averageGrade(name).getOrElse("No Grades Available")}")
      }
    }
    println("Students by Average Grade:")
    listStudentsByAverageGrade.foreach {
      case (name, avg) => println(s"$name: $avg")
    }
  }
}
// Higher order functions
def map[A, B](xs: List[A], f: A => B): List[B] = xs.map(f)

val doubledList = map(List(1, 2, 3, 4, 5), x => x * 2)

// Lexical closures
def makeCounter(): () => Int = {
  var counter = 0
  def increment(): Int = {
    counter += 1
    counter
  }
  increment
}

val counter = makeCounter()
counter() // 1
counter() // 2
counter() // 3

// Pattern matching
def sum(xs: List[Int]): Int = xs match {
  case Nil => 0
  case h :: t => h + sum(t)
}

val sumList = sum(List(1, 2, 3, 4, 5)) // 15

// Single assignment
val myVariable = 123

// Lazy evaluation
val myLazyVal = {
  println("Evaluating myLazyVal...")
  123
}

// Type inference
val myList: List[Int] = List(1, 2, 3, 4, 5)

// Tail call optimization
def factorial(n: Int): Int = if (n == 0) 1 else n * factorial(n - 1)

// List comprehensions
val evenList = List(1, 2, 3, 4, 5).filter(_ % 2 == 0)

// Monadic effects
def readFile(fileName: String): Option[String] = {
  try {
    Some(scala.io.Source.fromFile(fileName).mkString)
  } catch {
    case e: Throwable => None
  }
}

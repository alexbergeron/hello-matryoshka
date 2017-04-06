package example

import scalaz._
import matryoshka._
import matryoshka.data._
import matryoshka.implicits._

object Hello extends Greeting with App {
  println(evaledGreeting)
  println(coevaled)
  println(evaled2)
  println(hyloevaled)
}

trait Greeting {

  sealed trait Expr[A]
  final case class Word[A](value: String, r: A) extends Expr[A]
  final case class EOS[A]() extends Expr[A]

  implicit val exprFunctor = new Functor[Expr] {
    override def map[A, B](fa: Expr[A])(f: A => B) = fa match {
      case Word(v, r) => Word[B](v, f(r))
      case EOS() => EOS[B]()
    }
  }

  val eval: Algebra[Expr, String] = {
    case Word(w, r) => s"$w $r"
    case EOS() => ""
  }

  def someGreeting[T](implicit T: Corecursive.Aux[T, Expr]): T =
    Word[T]("hello", Word[T]("world", EOS[T]().embed).embed).embed

  val evaledGreeting = someGreeting[Fix[Expr]].cata(eval)

  val coeval: Coalgebra[Expr, String] = {
    case "" => EOS()
    case s => 
      val (h, t) = s.span(_ != ' ')
      Word(h, t.trim)
  }

  val coevaled = "hello world".ana[Fix[Expr]](coeval)
  val evaled2 = coevaled.cata(eval)
  val hyloevaled = "hello world".hylo[Expr, String](eval, coeval)

}

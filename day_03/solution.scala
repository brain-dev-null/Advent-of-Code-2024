import ParserState.*
import Instruction.*

import scala.io.Source

@main
def main(): Unit = {
  val source = Source.fromFile("input.txt")
  val text = source.mkString

  solveA(text)
  solveB(text)

  source.close
}

def solveA(text: String): Unit = {
  val instructions = text.foldLeft(Parser.init)(parse).instructions
  val result = interpretInstructionsA(instructions)
  System.out.println(result)
}

def solveB(text: String): Unit = {
  val instructions = text.foldLeft(Parser.init)(parse).instructions
  val result = interpretInstructionsB(instructions)
  System.out.println(result)
}

enum Instruction:
  case Do
  case Dont
  case Mul(left: Int, right: Int)

sealed trait State:
  def sum: Int

object State:
  def active: State = Active(0)

case class Active(sum: Int) extends State
case class InActive(sum: Int) extends State

def interpretInstructionsA(instructions: List[Instruction]): Int =
  instructions.foldLeft(0)((sum, instruction) => instruction match {
    case Mul(left, right) => + left * right
    case _ => sum
  })

def interpretInstructionsB(instructions: List[Instruction]): Int =
  instructions.foldLeft(State.active)((state, instruction) => (state, instruction) match {
    case (Active(sum), Mul(left, right)) => Active(sum + left * right)
    case (InActive(sum), Do) => Active(sum)
    case (Active(sum), Dont) => InActive(sum)
    case _ => state
  }).sum


enum ParserState:
  case Searching(acc: String)
  case DoStarted
  case DontStarted
  case FactorA(acc: String)
  case FactorB(factorA: Int, acc: String)

case class Parser(instructions: List[Instruction], state: ParserState)
object Parser:
  def init = Parser(List.empty, Searching(""))

def parse(parser: Parser, nextLetter: Char): Parser =
  val instructions = parser.instructions
  val state = parser.state
  (state, nextLetter) match
    case (Searching(acc), '(') if acc.endsWith("mul") => parser.copy(state = FactorA(""))
    case (Searching(acc), '(') if acc.endsWith("don't") => parser.copy(state = DontStarted)
    case (Searching(acc), '(') if acc.endsWith("do") => parser.copy(state = DoStarted)
    case (Searching(acc), _) => parser.copy(state = Searching(acc + nextLetter))

    case (DoStarted, ')') => Parser(instructions.appended(Do), Searching(""))
    case (DontStarted, ')') => Parser(instructions.appended(Dont), Searching(""))

    case (FactorA(acc), _) if nextLetter.isDigit => parser.copy(state = FactorA(acc.appended(nextLetter)))
    case (FactorA(acc), ',') => parser.copy(state = FactorB(acc.toInt, ""))

    case (FactorB(factorA, acc), _) if nextLetter.isDigit => parser.copy(state = FactorB(factorA, acc.appended(nextLetter)))
    case (FactorB(factorA, acc), ')') => Parser(instructions.appended(Mul(factorA, acc.toInt)), Searching(""))

    case _ => parser.copy(state = Searching(""))

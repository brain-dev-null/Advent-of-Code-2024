import ParserState.*
import Instruction.*

import scala.io.Source

@main
def main(): Unit = {
  val source = Source.fromFile("input.txt")
  val text = source.getLines.toList.foldLeft("")((s1, s2) => s1.concat(s2))

  solveA(text)
  solveB(text)

  source.close
}

def solveA(text: String): Unit = {
  val instructions = text.foldLeft((List.empty: List[Instruction], Searching("")))((a, b) => parseNext(a._1, a._2, b))._1
  val result = interpretInstructionsA(instructions)
  System.out.println(result)
}

def solveB(text: String): Unit = {
  val instructions = text.foldLeft((List.empty: List[Instruction], Searching("")))((a, b) => parseNext(a._1, a._2, b))._1
  val result = interpretInstructionsB(instructions)
  System.out.println(result)
}

enum Instruction:
  case Do
  case Dont
  case Mul(left: Int, right: Int)

def interpretInstructionsA(instructions: List[Instruction]): Int =
  instructions.foldLeft(0)((acc, instruction) => instruction match {
    case Mul(left, right) => acc + left * right
    case _ => acc
  })

def interpretInstructionsB(instructions: List[Instruction]): Int =
  instructions.foldLeft((0, true))((acc, instruction) => instruction match {
    case Mul(left, right) if acc._2 => acc.copy(_1 = acc._1 + left * right)
    case Do => acc.copy(_2 = true)
    case Dont => acc.copy(_2 = false)
    case _ => acc
  })._1

enum ParserState:
  case Searching(acc: String)
  case DoStarted
  case DontStarted
  case FactorA(acc: String)
  case FactorB(factorA: Int, acc: String)

def parseNext(prevElems: List[Instruction], state: ParserState, nextLetter: Char): (List[Instruction], ParserState) =
  (state, nextLetter) match
    case (Searching(acc), '(') if acc.endsWith("mul") => (prevElems, FactorA(""))
    case (Searching(acc), '(') if acc.endsWith("don't") => (prevElems, DontStarted)
    case (Searching(acc), '(') if acc.endsWith("do") => (prevElems, DoStarted)
    case (Searching(acc), _) => (prevElems, Searching(acc + nextLetter))

    case (DoStarted, ')') => (prevElems.appended(Do), Searching(""))
    case (DontStarted, ')') => (prevElems.appended(Dont), Searching(""))

    case (FactorA(acc), _) if nextLetter.isDigit => (prevElems, FactorA(acc.appended(nextLetter)))
    case (FactorA(acc), ',') => (prevElems, FactorB(acc.toInt, ""))

    case (FactorB(factorA, acc), _) if nextLetter.isDigit => (prevElems, FactorB(factorA, acc.appended(nextLetter)))
    case (FactorB(factorA, acc), ')') => (prevElems.appended(Mul(factorA, acc.toInt)), Searching(""))

    case _ => (prevElems, Searching(""))
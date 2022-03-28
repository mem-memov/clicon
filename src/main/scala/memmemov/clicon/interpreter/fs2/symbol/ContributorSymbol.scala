package memmemov.clicon.interpreter.fs2.symbol

import memmemov.clicon.algebra.symbol as AS

case class ContributorSymbol(
                              input: AS.StreamSymbol,
                              output: AS.StreamSymbol) extends AS.ContributorSymbol

package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol

trait Contributor[R[_]]:

  def createContributor(input: R[symbol.Stream], output: R[symbol.Stream]): R[symbol.Contributor]

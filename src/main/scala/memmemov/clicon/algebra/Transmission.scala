package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol

trait Transmission[R[_]]:

  def createTransmission(): R[symbol.Transmission]

  def plugContributor(transmission: R[symbol.Transmission], contributor: R[symbol.Contributor]): R[symbol.Transmission]
  
  def unplugContributor(transmission: R[symbol.Transmission], contributor: R[symbol.Contributor]): R[symbol.Transmission]

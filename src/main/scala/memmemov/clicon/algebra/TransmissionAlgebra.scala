package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol.{ContributorSymbol, TransmissionSymbol}

trait TransmissionAlgebra[R[_]]:

  def createTransmission(): R[TransmissionSymbol]

  def plugContributor(transmission: R[TransmissionSymbol], contributor: R[ContributorSymbol]): R[TransmissionSymbol]
  
  def unplugContributor(transmission: R[TransmissionSymbol], contributor: R[ContributorSymbol]): R[TransmissionSymbol]

package memmemov.clicon.algebra

trait TransmissionAlgebra[R[_]]:

  type Contributor
  type Transmission

  def createTransmission(): R[Transmission]

  def plugContributor(transmission: R[Transmission], contributor: R[Contributor]): R[Transmission]
  
  def unplugContributor(transmission: R[Transmission], contributor: R[Contributor]): R[Transmission]

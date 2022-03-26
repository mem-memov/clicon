package memmemov.clicon.algebra

trait TransmissionAlgebra[Transmission]:

  type Contributor

  def createTransmission(): Transmission

  def plugContributor(transmission: Transmission, contributor: Contributor): Transmission
  
  def unplugContributor(transmission: Transmission, contributor: Contributor): Transmission

package memmemov.clicon.algebra

trait TransmissionAlgebra[Transmission]:

  def createTransmission(): Transmission

  def plugContributor(transmission: Transmission, contributor: ContributorSymbol): Transmission
  
  def unplugContributor(transmission: Transmission, contributor: ContributorSymbol): Transmission

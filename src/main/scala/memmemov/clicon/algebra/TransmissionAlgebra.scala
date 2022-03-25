package memmemov.clicon.algebra

trait TransmissionAlgebra[F[_], Transmission, Contributor]:

  def createTransmission(): F[Transmission]

  def plugContributor(transmission: Transmission, contributor: Contributor): F[Transmission]
  
  def unplugContributor(transmission: Transmission, contributor: Contributor): F[Transmission]

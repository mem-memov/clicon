package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol.Contributor

trait TransmissionAlgebra[T]:

  def createTransmission(initiator: Contributor, approver: Contributor): T

  def plugContributor(transmission: T, contributor: Contributor): T
  
  def unplugContributor(transmission: T, contributor: Contributor): T

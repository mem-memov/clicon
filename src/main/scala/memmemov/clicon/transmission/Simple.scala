package memmemov.clicon.transmission

import memmemov.clicon.transmission.Algebra


case class Simple[A](value: A)

given simpleAlgebra[S]: Algebra[Simple] = new Algebra[Simple]:

  type Stream = Option[S]

  case class Contributor(input: Stream, output: Stream)

  case class Transmission(initiator: Contributor, approver: Contributor)

  override def stream(stream: Stream): Simple[Stream] = 
    Simple(stream)

  override def contributor(input: Simple[Stream], output: Simple[Stream]): Simple[Contributor] = 
    Simple(Contributor(input.value, output.value))

  override def transmission(initiator: Simple[Contributor], approver: Simple[Contributor]): Simple[Transmission] = 
    Simple(Transmission(initiator.value, approver.value))

  override def plugContributor(transmission: Simple[Transmission], contributor: Simple[Contributor]): Simple[Transmission] = 
    transmission.value match
      case Transmission(Contributor(None, None), approver) => Simple(Transmission(contributor.value, approver))
      case Transmission(initiator, Contributor(None, None)) => Simple(Transmission(initiator, contributor.value))
      case _ => ???

  override def unplugContributor(transmission: Simple[Transmission], contributor: Simple[Contributor]): Simple[Transmission] = ???

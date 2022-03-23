package memmemov.clicon.transmission

trait Algebra[T[_]]:

  type Stream
  type Contributor
  type Transmission

  def stream(stream: Stream): T[Stream]

  def contributor(input: T[Stream], output: T[Stream]): T[Contributor]

  def transmission(initiator: T[Contributor], approver: T[Contributor]): T[Transmission]

  def plugContributor(transmission: T[Transmission], contributor: T[Contributor]): T[Transmission]

  def unplugContributor(transmission: T[Transmission], contributor: T[Contributor]): T[Transmission]

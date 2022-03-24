package memmemov.clicon.transmission

trait Algebra[T[_], Stream]:

  type Contributor
  type Transmission

  def stream(stream: Option[Stream]): T[Option[Stream]]

  def contributor(input: T[Option[Stream]], output: T[Option[Stream]]): T[Contributor]

  def transmission(initiator: T[Contributor], approver: T[Contributor]): T[Transmission]

  def plugContributor(transmission: T[Transmission], contributor: T[Contributor]): T[Transmission]

  def unplugContributor(transmission: T[Transmission], contributor: T[Contributor]): T[Transmission]

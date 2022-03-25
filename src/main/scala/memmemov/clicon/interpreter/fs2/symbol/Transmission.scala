package memmemov.clicon.interpreter.fs2.symbol

import memmemov.clicon.interpreter.fs2.symbol.Contributor

case class Transmission(initiator: Option[Contributor], approver: Option[Contributor])
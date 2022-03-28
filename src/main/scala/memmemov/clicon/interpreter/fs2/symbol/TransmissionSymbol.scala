package memmemov.clicon.interpreter.fs2.symbol

import memmemov.clicon.algebra.symbol as AS

case class TransmissionSymbol(
                               initiator: Option[AS.ContributorSymbol],
                               approver: Option[AS.ContributorSymbol]) extends AS.TransmissionSymbol

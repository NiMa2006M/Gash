package com.example.gash.domain.error

sealed class DomainError(cause: Throwable? = null) : Exception(cause) {

    data object InvalidActivationCode : DomainError()

    data object EmptyRfidCode : DomainError()
    class RfidAssignmentFailed(cause: Throwable? = null) : DomainError(cause)
    data object AnimalNotFound : DomainError()
    data object AnimalExpired : DomainError()
    data object RfidCodeActiveOnAnotherAnimal : DomainError()
    data object EmptyHerdName : DomainError()
    data object InvalidWeight : DomainError()
    data object EmptyFarmName : DomainError()

    class Unknown(cause: Throwable? = null) : DomainError(cause)
}
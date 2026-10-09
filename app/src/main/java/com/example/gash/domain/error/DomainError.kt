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

    class InvalidFarmInfo(
        val farmNameEmpty: Boolean,
        val farmIdEmpty: Boolean,
        val phoneNumberEmpty: Boolean
    ) : DomainError()

    class ProfileImageFailed(cause: Throwable? = null) : DomainError(cause)

    class Unknown(cause: Throwable? = null) : DomainError(cause)
}
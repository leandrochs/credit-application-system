package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

class CreditServiceTest {
    private val creditRepository: CreditRepository = mockk()
    private val customerRepository: CustomerRepository = mockk()
    private val customerService = CustomerService(customerRepository)
    private val creditService = CreditService(creditRepository, customerService)

    @Test
    fun `should save credit`() {
        // given
        val fakeCustomer: Customer = buildCustomer(id = 1L)
        every { customerRepository.findById(any()) } returns Optional.of(fakeCustomer)

        val fakeCredit = buildCredit(customer = fakeCustomer)
        every { creditRepository.save(fakeCredit) } returns fakeCredit

        // when
        val savedCredit: Credit = creditService.save(fakeCredit)

        // then
        assertThat(savedCredit).isNotNull
        assertThat(savedCredit).isEqualTo(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }



    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.JUNE, 22),
        numberOfInstallments: Int = 5,
        customer: Customer
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )

    private fun buildCustomer(
        firstName: String = "Ana",
        lastName: String = "Maria",
        cpf: String = "02730702075",
        email: String = "ana@gmail.com",
        password: String = "123456",
        zipCode: String = "123456",
        street: String = "Rua A",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long,
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id,
    )
}

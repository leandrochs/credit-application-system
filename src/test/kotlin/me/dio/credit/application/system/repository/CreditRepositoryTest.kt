package me.dio.credit.application.system.repository

import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Autowired
    lateinit var testEntityManager: TestEntityManager

    private lateinit var customer1: Customer
    private lateinit var customer2: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @BeforeEach
    fun setup() {
        customer1 = testEntityManager.persist(buildCustomer1())
        customer2 = testEntityManager.persist(buildCustomer2())
        credit1 = testEntityManager.persist(buildCredit(customer = customer1))
        credit2 = testEntityManager.persist(buildCredit(customer = customer2))
    }

    @Test
    fun `should find credit by credit code`() {
        // given
        val creditCode1 = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        val creditCode2 = UUID.fromString("49f740be-46a7-449b-84e7-ff5b7986d7ef")
        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2

        // when
        val fakeCredit1 = creditRepository.findByCreditCode(creditCode1)!!
        val fakeCredit2 = creditRepository.findByCreditCode(creditCode2)!!
        // then
        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit2).isNotNull
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
        Assertions.assertThat(fakeCredit2).isSameAs(credit2)
        Assertions.assertThat(fakeCredit1.customer).isSameAs(credit1.customer)
        Assertions.assertThat(fakeCredit2.customer).isSameAs(credit2.customer)
        Assertions.assertThat(fakeCredit1.customer).isNotSameAs(credit2.customer)

    }

    private fun buildCredit(
            creditValue: BigDecimal = BigDecimal.valueOf(500.0),
            dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.APRIL, 22),
            numberOfInstallments: Int = 5,
            customer: Customer
    ): Credit = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customer = customer
    )

    private fun buildCustomer1(
            firstName: String = "Ana",
            lastName: String = "MAria",
            cpf: String = "02730702075",
            email: String = "ana@gmail.com",
            password: String = "123456",
            zipCode: String = "123456",
            street: String = "Rua A",
            income: BigDecimal = BigDecimal.valueOf(1000.0),
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
    )

    private fun buildCustomer2(
            firstName: String = "Ana2",
            lastName: String = "MAria2",
            cpf: String = "70799789046",
            email: String = "ana2@gmail.com",
            password: String = "123456",
            zipCode: String = "123456",
            street: String = "Rua A",
            income: BigDecimal = BigDecimal.valueOf(1000.0),
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
    )
}
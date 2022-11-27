package chapter03

import java.math.BigDecimal
import java.time.Month
import java.util.Currency
import java.util.Objects

class Money private constructor(
    val amount: BigDecimal,
    val currency: Currency,
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val money = other as Money
        return amount == money.amount && currency == money.currency
    }

    override fun hashCode(): Int {
        return Objects.hash(amount, currency)
    }

    fun add(that: Money): Money {
        require(currency == that.currency) {
            "cannot add Money values of different currencies"
        }
        return Money(amount.add(that.amount), currency)
    }

    companion object {
        fun of(amount: BigDecimal, currency: Currency): Money {
            return Money(
                amount.setScale(currency.defaultFractionDigits),
                currency
            )
        }
    }
}

fun main() {
    Money.of(BigDecimal.ONE, Currency.getInstance("11"))
}
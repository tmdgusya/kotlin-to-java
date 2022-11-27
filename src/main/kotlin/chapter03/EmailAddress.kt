package chapter03

import java.util.Objects

class EmailAddress(val localPort: String, val domain: String) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as EmailAddress
        return if (localPort != that.localPort) false else domain == that.domain
    }

    override fun hashCode(): Int {
        return Objects.hash(localPort, domain)
    }

    override fun toString(): String {
        return "$localPort@$domain"
    }

    companion object {
        fun parse(value: String): EmailAddress {
            val atIndex = value.lastIndexOf('@')
            require(!(atIndex < 1 || atIndex == value.length - 1)) { "Email Address must be two parts separated by '@'" }
            return EmailAddress(
                value.substring(0, atIndex),
                value.substring(atIndex + 1, value.length)
            )
        }
    }
}
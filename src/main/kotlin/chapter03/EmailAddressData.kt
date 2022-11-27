package chapter03

import java.util.Objects

data class EmailAddressData(val localPort: String, val domain: String) {

    override fun toString(): String {
        return "$localPort@$domain"
    }

    companion object {
        fun parse(value: String): EmailAddressData {
            val atIndex = value.lastIndexOf('@')
            require(!(atIndex < 1 || atIndex == value.length - 1)) { "Email Address must be two parts separated by '@'" }
            return EmailAddressData(
                value.substring(0, atIndex),
                value.substring(atIndex + 1, value.length)
            )
        }
    }
}
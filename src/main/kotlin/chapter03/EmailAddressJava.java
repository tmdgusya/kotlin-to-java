package chapter03;

import java.util.Objects;

public class EmailAddressJava {
    private final String localPort;
    private final String domain;

    public static EmailAddress parse(String value) {
        var atIndex = value.lastIndexOf('@');
        if (atIndex < 1 || atIndex == value.length() - 1) {
            throw new IllegalArgumentException("Email Address must be two parts separated by '@'");
        }
        return new EmailAddress(
                value.substring(0, atIndex),
                value.substring(atIndex + 1, value.length())
        );
    }

    public EmailAddressJava(String localPort, String domain) {
        this.localPort = localPort;
        this.domain = domain;
    }

    public String getLocalPort() {
        return localPort;
    }

    public String getDomain() {
        return domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailAddressJava that = (EmailAddressJava) o;

        if (!Objects.equals(localPort, that.localPort)) return false;
        return Objects.equals(domain, that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localPort, domain);
    }

    @Override
    public String toString() {
        return localPort + '@' + domain;
    }
}
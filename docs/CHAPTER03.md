# CHAPTER03

Java 에서 Email 을 저장하는 객체를 만든다고 하면 아래와 같이 작성할 것이다.  

```java
public class EmailAddress {
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

    public EmailAddress(String localPort, String domain) {
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

        EmailAddress that = (EmailAddress) o;

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
```
이 Java 코드에서는 `@NotNull` 및 `@Nullable` 어노테이션이 별도로 존재하지 않으므로 **Null 가능성에 대한 관습**을 볼 수 없다.
이 객체는 Data 를 담는 Data 객체이므로 값들은 대부분 **불변인 final 로 선언**해준다. 
또한 Java 에서 변수에 final 한정자를 붙이게 되면 반드시 **constructor** 에서 정의 되어야 한다. 

이 코드가 코틀린으로 바뀌면 어떻게 변할까?

```kotlin
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
```

### final 한정자 리팩터링

인텔리제이의 기능인 Convert Java File to Kotlin File 을 이용하게 되면 위와 같이 변경된다. 
살펴보면 첫번째로 `localPort` 와 `domain` 이 **주 생성자(primary constructor) 안으로** 들어갔다. 
우리가 Java 코드를 생각해보면 `final` 한정자를 붙이게 되면 **초기화(initialize)** 가 되어야 하는데 그렇다면 constructor 에서 값을 받아 초기화 시키거나 값을 지정하여 초기화를 시켜주어야 한다. 
그래서 코틀린은 위와 같이 연관되어 있는 부분을 한곳으로 몰았다.

또한 `final` 을 붙이게 되면 객체는 불변상태가 되므로 값을 변경할 수 없다. 그래서 코틀린에서도 `val` 을 이용하여 값을 변경할 수 없도록 한다. 
이 특징을 잘 모르겠다면 kotlin 의 `[delegated properties](https://kotlinlang.org/docs/delegated-properties.html)` 를 보면 좋다.

### 정적 메소드 리팩터링

정적 메소드는 `companion object` 안으로 들어가 있는 것을 확인할 수 있다. 
쉽게 설명하면 Inner Class 로 `Companion` 클래스가 생기고 해당 객체를 생성하여 이용할 수 있게끔 된다.

```kotlin
EmailAddress.Companion().parse()
```

진짜 Java 처럼 재할당이 아닌 JVM 상에서 `static` 하게 이용하고 싶다면 `@JvmStatic` 어노테이션을 붙여주면 된다. 
하지만 코틀린에서는 top-class function 으로 리팩터링 하는게 더 좋은 수단일수도 있다.

### Getter 의 제거

위에서 언급한 위임된 속성(delegated properties) 이라는 기능을 이용하여 코틀린에서는 명시적으로 멤버 변수에 대한 getter 를 작성하지 않아도 된다. 
delegated 는 override 도 되니 아래 공식 문서를 읽어보면 좋다. 

[delegated properties](https://kotlinlang.org/docs/delegated-properties.html)

## Data Class 를 사용한 리팩터링

아까도 말했지만 현재 `EmailAddress` 클래스는 이메일 정보를 나타낸 `data` 형태의 클래스에 가깝다. 즉, 클래스 자체가 `값` 으로서 평가될 수 있다는 것이다. 
코틀린에서 data class 를 이용하게 되면 `equals`, `hashcode`, `toString` 메소드를 자동으로 생성해준다. 
따라서 Email Address 클래스를 값 클래스로 바꾸게 되면 좀 더 간단한 클래스가 된다. 

```kotlin
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
```

## Data Class 의 한계

Kotlin Data Class 에는 편리한 기능 중 하나인 `copy` 라는 기능이 있다. 

```kotlin
val postmasterEmail = email.copy(localPart = "postmatser")
```

위와 같이 새롭게 객체를 만들때, **현재 모든 속성을 유지하고 새롭게 값을 입력한 부분만 대치**해주는 기능이 있다. 
만약 위와 같은 방법으로 새롭게 값을 생성한다면 **캡슐화** 가 위반되는 것이므로 조심해야 한다. 
그리고 클래스자체의 값관에 어떠한 상황에도 **불변조건** 을 유지해야 하는 경우에 `copy` 메소드가 불변조건을 깨게 할 수 있다. 

여기서 **불변 조건**이란, 프로그래밍의 immutable 이라기 보단 속성간의 불변 조건이라고 이해하면 쉽다. 
예를 들면, **1kg = 1000g** 이라고 하면 **kg = 1000g** 을 나타내는 불변의 조건 이라고 보면 된다.

```kotlin
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
```

위와 같은 클래스를 `data class` 로 변경하게 되면 IDE 에서 경고를 알려주는데 `private constructor` 가 `copy` 메소드에 의해 노출될 수 있다고 말해준다. 
즉, 위 코드에서 of 를 통해 불변 조건을 유지하고 있는데, `copy` 를 통해 객체를 생성할 수 있게 되어 캡슐화를 위반하게 되는 것이다. 
이렇게 되어 **currency 가 가르키는 통화의 보조 통화 단위 수단과 money 가 일치해야 한다**는 불변조건이 깨지게 된다.  








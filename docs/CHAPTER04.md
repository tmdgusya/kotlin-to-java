# CHAPTER04

Null 가능성 하면 항상 나오는 명언이 하나 있다. 토니 호어(Tony Hoare) 가 말한 Null 참조를 만든게 10억 불 짜리 실수 였다. 라고 말하는 것이다.  

Rust, Kotlin 등등 현대적인 언어에서는 이 Null 가능성에 대한 예외를 컴파일 시간에서 최대한 해결하기 위해 Nullable 에 대한 개념등등을 코드안에 넣었다. 
Java 에서도 Optional Type 이 도입됬지만, Optional Type 을 쓰지 않는다면 여전히 Null 에 관련된 문제에 빠져든다. 

## Maybe Type 

함수형 프로그래머 들은 종종 코틀린 언어의 Nullable Spec 보다 Optional Type 을 사용하라고 권장한다고 한다. 
Optional Type 을 쓰면 **부재, 오류, 비동기 상황 등을 도구를 사용해 모두 처리**할 수 있지만 코틀린에서 Optional 을 쓰면 
안되는 이유는 널 가능성을 지원하기 위해 구체적으로 설계된 언어의 지원을 사용하지 못하게 된다는 것이다. 

즉, 해당 코드를 사용하는 부분을 나중에 Kotlin Nullable Type 으로 변경할때 널 가능성에 대한 작성이 없는 Java 코드를 변경하는 것과 
비슷한 수준의 문제를 겪게 된다. 

## NoSuchElementException 과 NullPointerException

Kotlin 에서 **Array 에 get([index]) 를 할 때 원소를 찾지 못하면 NoSuchElementException** 이 발생하게 된다. 
생각해보면 **NullPointerException** 과 다른게 무엇일까? 최근에 프로젝트를 진행했을때 [index] 를 써서 get 하는 부분들을 `.getOrNull()` 
을 이용하고 **?: (elvis operator)** 를 통해 failover 처리를 해주었더니 좀 더 에러를 찾기도 쉬웠고, 더 안전한 코드로 작성되는걸 경험했다.

책에서도 이야기하지만 코틀린과 같이 컴파일 타임에 안정성을 올려야 한다면 단순히 get operator 를 쓰는걸 주의해야 할 것 같다. 
쉽게 쓰고 싶다면 앞에서 size assertions 를 해주는 것도 방법이라고 생각한다.






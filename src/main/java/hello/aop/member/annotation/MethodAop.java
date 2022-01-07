package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Method 에 사용할 수 있는 어노티에션
@Retention(RetentionPolicy.RUNTIME) // 애플리케이션이 실행될 때까지 어노테이션이 살아있다.
public @interface MethodAop {
    String value(); // 어노테이션도 값을 가질 수 있다.
}

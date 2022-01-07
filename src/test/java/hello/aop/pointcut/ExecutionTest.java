package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(); // 포인트컷 표현식을 처리해주는 클래스
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); // hello() 메서드를 정보를 찾는다.
    }

    @Test
    public void printMethod() throws Exception {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }
}

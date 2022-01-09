package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    public void exactMatch() throws Exception {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void allMatch() throws Exception {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void nameMatchStar1() throws Exception {
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void nameMatchStar2() throws Exception {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void nameMatchFalse() throws Exception {
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    public void packageExactMatch1() throws Exception {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void packageExactMatch2() throws Exception {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void packageExactFalse() throws Exception {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    public void packageMatchSubPackage1() throws Exception {
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void packageMatchSubPackage2() throws Exception {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void typeExactMatch() throws Exception {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void typeMatchSuperType() throws Exception {
        // execution 에서는 MemberService 처럼 부모 타입으로 선언해도 그 자식 타입은 매칭된다.
        // 다형성에서 부모타입에 자식 타입이 할당 가능하다는 점을 떠올려보면 된다.
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void typeMatchInternal() throws Exception {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    public void typeMatchNoSuperTypeMethodFalse() throws Exception {
        // 부모 타입(MemberService)을 표현식에 선언한 경우 부모 타입에서 선언한 메서드가 자식 타입에 있어야 매칭에 성공한다.
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

        // 자식 타입(MemberServiceImpl)에서만 선언한 메서드는 매칭에 실패한다.
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * String 타입의 파라미터 허용 (String)
     * @throws Exception
     */
    @Test
    public void argsMatch() throws Exception {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 파라미터가 없어야 함 ()
     * @throws Exception
     */
    @Test
    public void argsMatchNoArgs() throws Exception {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 정확히 하나의 파라미터 허용, 모든 타입 허용 (Xxx)
     * @throws Exception
     */
    @Test
    public void argsMatchStar() throws Exception {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 숫자와 무관하게 모든 파라미터, 모든 타입 허용
     * (), (Xxx), (Xxx, Xxx)
     * @throws Exception
     */
    @Test
    public void argsMatchAll() throws Exception {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
     * (String), (String, Xxx), (String, Xxx, Xxx)
     * @throws Exception
     */
    @Test
    public void argsMatchComplex() throws Exception {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}

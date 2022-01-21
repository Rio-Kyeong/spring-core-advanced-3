package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

/**
 * 정리
 * JDK 동적 프록시는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 없다.
 * CGLIB 프록시는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 있다.
 */
@Slf4j
public class ProxyCastingTest {

    @Test
    public void jdkProxy() throws Exception {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시 사용

        // 프록시를 인터페이스로 캐스팅 성공
        // JDK 동적 프록시는 인터페이스 기반으로 프록시를 생성하기 때문에 캐스팅이 가능하다.
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // JDK 동적 프록시를 구현 클래스로 캐스팅 시도 실패, ClassCastException 예외 발생
        // JDK 동적 프록시는 인터페이스로 캐스팅 가능하지만 구현 객체 타입으로는 캐스팅이 불가능하다.
        Assertions.assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });
    }

    @Test
    public void cglibProxy() throws Exception {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // CGLIB 프록시 사용

        // 프록시를 인터페이스로 캐스팅 성공
        // CGLIB 프록시는 구체 클래스 기반으로 프록시를 생성한다.
        // 여기서 구체 클래스는 인터페이스(MemberService)를 구현했기 때문에 캐스팅이 가능하다.
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        log.info("proxy class={}", memberServiceProxy.getClass());

        // CGLIB 프록시를 구현 클래스로 캐스팅 시도 성공
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}

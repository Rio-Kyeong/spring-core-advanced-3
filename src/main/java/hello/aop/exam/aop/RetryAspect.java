package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    // @Around("@annotation(hello.aop.exam.annotation.Retry)")
    // 어노테이션 경로를 길게 지정해 줄 필요없이 어드바이스에 선언한 파라미터를 통해 지정할 수 있다.
    @Around("@annotation(retry)")
    public Object doRetry (ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value(); // 해당 예제는 재시도를 4번까지로 지정
        Exception exceptionHolder = null;

        for(int retryCount = 1; retryCount <= maxRetry; retryCount++){
            try {
                log.info("[retry] try count={}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            }catch (Exception e){
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}

package es.upm.miw.config;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogoutTimeAspect {

    @Before(value = "@annotation(RegisterTimeClock)")
    public void logoutTimeClock(JoinPoint joinPoint) {

        Object[] lArgs = joinPoint.getArgs();
        User activeUser = (User) lArgs[0];
        String log = "<<< logoutTimeClock << " + activeUser.getAuthorities().toString() +" ::::: " + joinPoint.getSignature().getName();
        if (log.length() > 1000) {
            log = log.substring(0, 1000) + ".... (+" + log.length() + " characters)";
        }
        LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName()).info(log);
    }

}

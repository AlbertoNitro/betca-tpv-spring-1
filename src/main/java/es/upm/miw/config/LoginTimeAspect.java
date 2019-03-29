package es.upm.miw.config;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginTimeAspect {

    @AfterReturning(value = "@annotation(RegisterTimeClock)")
    public void loginTimeClock(JoinPoint joinPoint) {

        Object[] lArgs = joinPoint.getArgs();
        User activeUser = (User) lArgs[0];

        /*activeUser.getAuthorities().stream().forEach(grantedAuthority -> {
                    if (grantedAuthority.getAuthority().matches("ROLE_OPERATOR") || grantedAuthority.getAuthority().matches("ROLE_MANAGER")) {
                        LogManager.getLogger().info("Se registra");
                    }
                }
        );*/
        String log = "<<< loginTimeClock << " + activeUser.getAuthorities().toString() +" ::::: " + joinPoint.getSignature().getName();
        if (log.length() > 1000) {
            log = log.substring(0, 1000) + ".... (+" + log.length() + " characters)";
        }
        LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName()).info(log);
    }
}
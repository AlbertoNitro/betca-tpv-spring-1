package es.upm.miw.config;

import es.upm.miw.documents.Role;
import es.upm.miw.documents.TimeClock;
import es.upm.miw.repositories.TimeClockRepository;
import es.upm.miw.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
public class TimeClockAspect {

    @Autowired
    private TimeClockRepository timeClockRepository;

    @Autowired
    private UserRepository userRepository;

    @Pointcut("execution(* es.upm.miw.rest_controllers.UserResource.logout(..))")
    public void saveLogoutTimeClock() {
        // Empty
    }

    @Pointcut("execution(* es.upm.miw.rest_controllers.UserResource.login(..))")
    public void saveLoginTimeClock() {
        // Empty
    }

    @Before("saveLogoutTimeClock()")
    public void logoutTimeClock(JoinPoint joinPoint) {
        Object[] lArgs = joinPoint.getArgs();
        String activeUserMobile = (String) lArgs[0];
        es.upm.miw.documents.User userLogged = this.userRepository.findByMobile(activeUserMobile).orElse(null);
        if (hasActiveUserRolOperatorManager(userLogged)) {
            TimeClock timeClockUpdated = saveClockOutActiveUser(userLogged);
            String log = "<<< logoutTimeClock Return << " + joinPoint.getSignature().getName() + ": " + timeClockUpdated.toString();
            LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName()).info(log);
        }
    }

    private TimeClock saveClockOutActiveUser(es.upm.miw.documents.User userLogged) {
        TimeClock timeClockActiveUser = this.timeClockRepository.findFirst1ByUserOrderByClockinDateDesc(userLogged.getId());
        timeClockActiveUser.clockout();
        return this.timeClockRepository.save(timeClockActiveUser);
    }

    private boolean isActiveUserAuthenticated(User activeUser) {
        List<GrantedAuthority> authorities = activeUser.getAuthorities().stream().filter(grantedAuthority -> grantedAuthority.getAuthority().matches(RoleType.ROLE_AUTHENTICATED.name())).collect(Collectors.toList());
        return !authorities.isEmpty();
    }

    @AfterReturning("saveLoginTimeClock()")
    public void loginTimeClock(JoinPoint joinPoint) {
        Object[] lArgs = joinPoint.getArgs();
        User activeUser = (User) lArgs[0];
        es.upm.miw.documents.User userLogged = this.userRepository.findByMobile(activeUser.getUsername()).orElse(null);
        if (isActiveUserAuthenticated(activeUser) && hasActiveUserRolOperatorManager(userLogged)) {
            // TODO: Controlar que no exista ya un registro para ese usuario en ese mismo día. Verificar
            // que es el primer login.
            // TimeClock firstTimeClock = this.timeClockRepository.findFirst1ByUserOrderByClockinDateDesc(userLogged.getId());
            TimeClock timeClockCreated = this.timeClockRepository.save(new TimeClock(userLogged));
            String log = "<<< loginTimeClock Return << " + joinPoint.getSignature().getName() + ": " + timeClockCreated.toString();
            LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName()).info(log);
        }
    }

    public boolean hasActiveUserRolOperatorManager(es.upm.miw.documents.User userLogged) {
        List<Role> roles = Arrays.asList(userLogged.getRoles());
        Role rol = roles.stream()
                .filter(role -> RoleType.ROLE_MANAGER.name().equals(role.roleName()) || RoleType.ROLE_OPERATOR.name().equals(role.roleName()))
                .findAny()
                .orElse(null);
        return rol != null;
    }
}
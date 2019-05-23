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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Aspect
public class UserAccessAspect {

    public enum RoleType {
        ROLE_AUTHENTICATED,
        ROLE_MANAGER,
        ROLE_OPERATOR;
    }

    @Autowired
    private TimeClockRepository timeClockRepository;

    @Autowired
    private UserRepository userRepository;

    @Pointcut("execution(* es.upm.miw.rest_controllers.UserResource.logout(..))")
    public void saveLogoutTimeClock() {
        // Empty but necessary for the implementation
    }

    @Pointcut("execution(* es.upm.miw.rest_controllers.UserResource.login(..))")
    public void saveLoginTimeClock() {
        // Empty but necessary for the implementation
    }

    @Before("saveLogoutTimeClock()")
    public void logoutTimeClock(JoinPoint joinPoint) {
        Object[] lArgs = joinPoint.getArgs();
        String activeUserMobile = (String) lArgs[0];
        es.upm.miw.documents.User userLogged = this.userRepository.findByMobile(activeUserMobile).orElse(null);
        if (userLogged != null && hasActiveUserRolOperatorManager(userLogged)) {
            TimeClock timeClockActiveUser = this.timeClockRepository.findFirst1ByUserOrderByClockinDateDesc(userLogged.getId()).orElse(null);
            if (timeClockActiveUser != null && sameDay(timeClockActiveUser)) {
                timeClockActiveUser.clockout();
                LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName()).info(timeClockActiveUser.toString());
                this.timeClockRepository.save(timeClockActiveUser);
                toLogResult(joinPoint, timeClockActiveUser, "[LogoutTimeClock Return] ::: ");
            }
        }
    }

    private boolean sameDay(TimeClock timeClockActiveUser) {
        return timeClockActiveUser.getClockoutDate().toLocalDate().isEqual(LocalDate.now());
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
        if (userLogged != null && isActiveUserAuthenticated(activeUser) && hasActiveUserRolOperatorManager(userLogged)) {
            TimeClock firstTimeClockUser = this.timeClockRepository.findFirst1ByUserOrderByClockinDateDesc(userLogged.getId()).orElse(null);
            if (firstTimeClockUser == null || !sameDay(firstTimeClockUser)) {
                TimeClock timeClockCreated = this.timeClockRepository.save(new TimeClock(userLogged));
                toLogResult(joinPoint, timeClockCreated, "[LoginTimeClock Return] ::: ");
            }
        }
    }

    private boolean hasActiveUserRolOperatorManager(es.upm.miw.documents.User userLogged) {
        List<Role> roles = Arrays.asList(userLogged.getRoles());
        Role rol = roles.stream()
                .filter(Objects::nonNull).filter(role -> RoleType.ROLE_MANAGER.name().equals(role.roleName()) || RoleType.ROLE_OPERATOR.name().equals(role.roleName()))
                .findAny()
                .orElse(null);
        return rol != null;
    }

    private void toLogResult(JoinPoint joinPoint, TimeClock timeClockUpdated, String s) {
        String log = s + joinPoint.getSignature().getName() + ": " + timeClockUpdated.toString();
        LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName()).info(log);
    }
}
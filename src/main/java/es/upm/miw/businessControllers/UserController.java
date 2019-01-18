package es.upm.miw.businessControllers;

import es.upm.miw.businessServices.JwtService;
import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.TokenOutputDto;
import es.upm.miw.dtos.UserDto;
import es.upm.miw.exceptions.ForbiddenException;
import es.upm.miw.exceptions.NotFoundException;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public TokenOutputDto login(String mobile) {
        User user = userRepository.findByMobile(mobile).get();
        String[] roles = Arrays.stream(user.getRoles()).map(Role::roleName).toArray(String[]::new);
        String token = jwtService.createToken(user.getMobile(), roles);
        return new TokenOutputDto(token, user.getRoles());
    }

    public UserDto readUser(String mobile, String claimMobile, List<String> claimRoles) throws NotFoundException, ForbiddenException {
        User user = this.userRepository.findByMobile(mobile)
                .orElseThrow(() -> new NotFoundException("User mobile:" + mobile));
        this.authorized(claimMobile, claimRoles, mobile, Arrays.asList(user.getRoles()).stream()
                .map(role -> role.roleName()).collect(Collectors.toList()));
        return new UserDto(user);
    }

    private void authorized(String claimMobile, List<String> claimRoles, String userMobile, List<String> userRoles)
            throws ForbiddenException {
        if (claimRoles.contains(Role.ADMIN.roleName()) || claimMobile.equals(userMobile)) {
            return;
        }
        if (claimRoles.contains(Role.MANAGER.roleName())
                && !userRoles.contains(Role.ADMIN.roleName()) && !userRoles.contains(Role.MANAGER.roleName())) {
            return;
        }
        if (claimRoles.contains(Role.OPERATOR.roleName())
                && userRoles.stream().allMatch(role -> role.equals(Role.CUSTOMER.roleName()))) {
            return;
        }
        throw new ForbiddenException("User mobile (" + userMobile + ")");
    }

}

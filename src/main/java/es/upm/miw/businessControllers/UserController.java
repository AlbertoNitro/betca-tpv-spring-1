package es.upm.miw.businessControllers;

import es.upm.miw.businessServices.JwtService;
import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.TokenOutputDto;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public TokenOutputDto login(String mobile) {
        User user = userRepository.findByMobile(mobile);
        assert user != null;
        String[] roles = Arrays.stream(user.getRoles()).map(Role::roleName).toArray(String[]::new);
        String token = jwtService.createToken(user.getUsername(), roles);
        return new TokenOutputDto(token, user.getRoles());
    }
}

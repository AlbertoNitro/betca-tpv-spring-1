package es.upm.miw.businessControllers;

import es.upm.miw.dtos.TokenOutputDto;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public TokenOutputDto login(String username) {
        return null;
    }
}

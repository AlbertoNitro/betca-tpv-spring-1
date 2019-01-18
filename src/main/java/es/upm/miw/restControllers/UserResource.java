package es.upm.miw.restControllers;

import es.upm.miw.businessControllers.UserController;
import es.upm.miw.dtos.TokenOutputDto;
import es.upm.miw.dtos.UserDto;
import es.upm.miw.exceptions.ForbiddenException;
import es.upm.miw.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    public static final String USERS = "/users";

    public static final String TOKEN = "/token";

    public static final String MOBILE_ID = "/{mobile}";

    @Autowired
    private UserController userController;

    @PreAuthorize("authenticated")
    @PostMapping(value = TOKEN)
    public TokenOutputDto login(@AuthenticationPrincipal User activeUser) {
        return userController.login(activeUser.getUsername());
    }

    @GetMapping(value = MOBILE_ID)
    public UserDto read(@PathVariable String mobile, @AuthenticationPrincipal User activeUser)
            throws NotFoundException, ForbiddenException {
        return this.userController.readUser(mobile, SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }


}

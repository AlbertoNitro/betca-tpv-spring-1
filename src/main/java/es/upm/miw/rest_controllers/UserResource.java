package es.upm.miw.rest_controllers;

import es.upm.miw.business_controllers.UserController;
import es.upm.miw.documents.Role;
import es.upm.miw.dtos.*;
import es.upm.miw.dtos.output.TokenOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    public static final String USERS = "/users";
    public static final String MINIMUM = "/minimum";

    public static final String TOKEN = "/token";
    public static final String ROLES = "/roles";
    public static final String QUERY = "/query";
    public static final String MOBILE_ID = "/{mobile}";
    public static final String PASSWORDS = "/passwords";

    @Autowired
    private UserController userController;

    @PreAuthorize("authenticated")
    @PostMapping(value = TOKEN)
    public TokenOutputDto login(@AuthenticationPrincipal User activeUser) {
        return userController.login(activeUser.getUsername());
    }

    @GetMapping(value = MOBILE_ID)
    public UserDto read(@PathVariable String mobile, @AuthenticationPrincipal User activeUser) {
        return this.userController.readUser(mobile, SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    @GetMapping
    public List<UserMinimumDto> readAll() {
        return this.userController.readAll();
    }

    @PostMapping(value = MINIMUM)
    public UserMinimumDto create(@Valid @RequestBody UserMinimumDto user) {
        return this.userController.createUserMinimum(user);
    }

    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto user) {
        return this.userController.create(user);
    }

    @PutMapping(value = MOBILE_ID)
    public UserDto update(@PathVariable String mobile, @Valid @RequestBody UserDto user) {
        return this.userController.update(mobile, user);
    }

    @PutMapping(value = ROLES+MOBILE_ID)
    public UserDto updateRoles(@PathVariable String mobile, @Valid @RequestBody UserRolesDto userRolesDto) {
        return this.userController.updateRoles(mobile, userRolesDto);
    }

    @PostMapping(value = QUERY)
    public List<UserMinimumDto> readQueryByRoles(@RequestBody UserQueryDto userQueryDto, @AuthenticationPrincipal User activeUser) {

        List<String> userRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Role[] authorities;
        if(userQueryDto.isOnlyCustomer()){
            authorities=new Role[]{Role.CUSTOMER};
        }else {
            String[] rolesLoing = userRoles.get(0).split("ROLE_");
            authorities = autorizationRole(Role.valueOf(rolesLoing[1]));
        }
        String mobile=userQueryDto.getMobile();
        String username=userQueryDto.getUsername();
        String dni=userQueryDto.getDni();
        String address=userQueryDto.getAddress();

        return this.userController.readAllByUsernameDniAddressRoles(mobile,username,dni,address,authorities);
    }

    public Role[] autorizationRole(Role userRoles){
        Role[] authorities;
        if(userRoles.equals(Role.ADMIN)) {
            authorities=new Role[]{Role.ADMIN,Role.MANAGER,Role.OPERATOR,Role.CUSTOMER};
        }else if(userRoles.equals(Role.MANAGER)){
            authorities=new Role[]{Role.MANAGER,Role.OPERATOR,Role.CUSTOMER};
        }else {
            authorities=new Role[]{Role.OPERATOR,Role.CUSTOMER};
        }
        return authorities;
    }

    @PutMapping(value = PASSWORDS+MOBILE_ID)
    public UserProfileDto updateProfile(@PathVariable String mobile, @Valid @RequestBody UserProfileDto userProfileDto) {
        return this.userController.updateProfile(mobile, userProfileDto);
    }
}

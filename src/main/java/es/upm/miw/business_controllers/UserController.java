package es.upm.miw.business_controllers;

import es.upm.miw.business_services.JwtService;
import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.UserProfileDto;
import es.upm.miw.dtos.UserRolesDto;
import es.upm.miw.dtos.output.TokenOutputDto;
import es.upm.miw.dtos.UserDto;
import es.upm.miw.dtos.UserMinimumDto;
import es.upm.miw.exceptions.BadRequestException;
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
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("Unexpected!!. Mobile not found:" + mobile));
        String[] roles = Arrays.stream(user.getRoles()).map(Role::name).toArray(String[]::new);
        return new TokenOutputDto(jwtService.createToken(user.getMobile(), user.getUsername(), roles));
    }

    public UserDto readUser(String mobile, String claimMobile, List<String> claimRoles) {
        User user = this.userRepository.findByMobile(mobile)
                .orElseThrow(() -> new NotFoundException("User mobile:" + mobile));
        this.authorized(claimMobile, claimRoles, mobile, Arrays.stream(user.getRoles())
                .map(Role::roleName).collect(Collectors.toList()));
        return new UserDto(user);
    }

    private void authorized(String claimMobile, List<String> claimRoles, String userMobile, List<String> userRoles) {
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

    public List<UserMinimumDto> readAll() {
        return this.userRepository.findAllUsers();
    }

    public UserMinimumDto createUserMinimum(UserMinimumDto userMinimumDto) {
        if(this.userRepository.findByMobile(userMinimumDto.getMobile()).isPresent()) {
            throw new BadRequestException("User mobile (" + userMinimumDto.getMobile() + ") already exist.");
        }

        User saved = User.builder().username(userMinimumDto.getUsername()).mobile(userMinimumDto.getMobile())
                .roles(new Role[]{Role.CUSTOMER}).build();

        this.userRepository.save(saved);
        return new UserMinimumDto(saved.getMobile(), saved.getUsername());
    }

    public UserDto create(UserDto userDto) {
        if(this.userRepository.findByMobile(userDto.getMobile()).isPresent()) {
            throw new BadRequestException("User mobile (" + userDto.getMobile() + ") already exist.");
        }
        User saved = User.builder().mobile(userDto.getMobile()).username(userDto.getUsername()).email(userDto.getEmail())
                .dni(userDto.getDni()).address(userDto.getAddress()).active(userDto.isActive()).roles(new Role[]{Role.CUSTOMER})
                .registrationDate(userDto.getRegistrationDate()).build();

        this.userRepository.save(saved);
        return new UserDto(saved);
    }

    public UserDto update(String mobile, UserDto userDto) {

        if(!mobile.equals(userDto.getMobile())) {
            throw new BadRequestException("User mobile (" + mobile + ")");
        }
        User userFound = this.userRepository.findByMobile(mobile)
                .orElseThrow(() -> new NotFoundException("User mobile (" + userDto.getMobile() + ") is not found."));

        User saved = User.builder().id(userFound.getId()).username(userDto.getUsername()).password(userFound.getPassword())
                .mobile(userDto.getMobile()).roles(userFound.getRoles()).dni(userDto.getDni()).address(userDto.getAddress())
                .email(userDto.getEmail()).registrationDate(userDto.getRegistrationDate()).active(userDto.isActive()).build();

        this.userRepository.save(saved);
        return new UserDto(saved);
    }

    public UserDto updateRoles(String mobile, UserRolesDto userRolesDto) {

    if (mobile == null || !mobile.equals(userRolesDto.getMobile()))
            throw new BadRequestException("User mobile (" + userRolesDto.getMobile() + ")");

        User user = this.userRepository.findByMobile(mobile).orElseThrow(() -> new NotFoundException("User mobile (" + mobile + ")"));;
        User result = this.userRepository.save(new User(user.getId(),user.getUsername(),user.getDni(),user.getEmail(),user.getAddress(),user.getPassword(),userRolesDto));
        return new UserDto(result);
    }

    public UserProfileDto updateProfile(String mobile, UserProfileDto userProfileDto) {

        if (mobile == null || !mobile.equals(userProfileDto.getMobile()))
            throw new BadRequestException("User mobile (" + userProfileDto.getMobile() + ")");

        User user = this.userRepository.findByMobile(mobile).orElseThrow(() -> new NotFoundException("User mobile (" + mobile + ")"));;
        User result = this.userRepository.save(new User(user.getId(),user.getUsername(),user.getDni(),user.getEmail(),user.getAddress(),user.getRoles(),userProfileDto));
        return new UserProfileDto(result);
    }

    public List<UserMinimumDto> readAllByUsernameDniAddressRoles(String mobile,String username,String dni, String address,  Role[] roles) {
       return this.userRepository.findByMobileUsernameDniAddressLikeNullSafeandRoles(mobile,username,dni,address,roles);
    }
}

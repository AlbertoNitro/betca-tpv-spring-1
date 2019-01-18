package es.upm.miw.dataServices;

import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final String P_TOKEN = "";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String mobile) {
        User user = userRepository.findByMobile(mobile);
        if (user != null) {
            return this.userBuilder(String.valueOf(user.getMobile()), user.getPassword(), new Role[]{Role.AUTHENTICATED},
                    user.isActive());
        } else {
            throw new UsernameNotFoundException("mobile not found. " + mobile);
        }
    }

    private org.springframework.security.core.userdetails.User userBuilder(String mobile, String password, Role[] roles,
                                                                           boolean active) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.roleName()));
        }
        return new org.springframework.security.core.userdetails.User(mobile, password, active, true,
                true, true, authorities);
    }
}

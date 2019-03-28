package com.cyber.spring.JsonCloud.service;

import com.cyber.spring.JsonCloud.entity.Role;
import com.cyber.spring.JsonCloud.entity.UserAccount;
import com.cyber.spring.JsonCloud.repository.RoleRepository;
import com.cyber.spring.JsonCloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount userAccount = userRepository.findByLogin(username).orElseThrow(
                () -> new UsernameNotFoundException("userAccount '" + username + "' not found")
        );

        return new User(
                userAccount.getLogin(),
                userAccount.getPassword(),
                mapRolesToAuthorities( userAccount.getRoles() )
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    private Collection<Role> bindRoles(Collection<Role> newRoles){
        if (newRoles==null) return null;
        return newRoles.stream()
                .map( newRole -> roleRepository.findByName(newRole.getName()).orElse(newRole) )
                .collect( Collectors.toList());
    }

    private boolean isPasswordEncrypted(String password){
        return password.startsWith("$2a$10$");
    }

    @Override
    public UserAccount save(UserAccount user) {

        String password = user.getPassword();
        if (password!=null && !password.isEmpty() && !isPasswordEncrypted(password)){
            user.setPassword( passwordEncoder.encode(password) );
        }

        user.setRoles( bindRoles( user.getRoles() ) );

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<UserAccount> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
        return userRepository.findById(id);
    }


}

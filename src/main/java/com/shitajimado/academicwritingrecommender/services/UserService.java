package com.shitajimado.academicwritingrecommender.services;

import com.shitajimado.academicwritingrecommender.core.exceptions.UserAlreadyExistsException;
import com.shitajimado.academicwritingrecommender.core.exceptions.UserNotExistsException;
import com.shitajimado.academicwritingrecommender.entities.Role;
import com.shitajimado.academicwritingrecommender.entities.User;
import com.shitajimado.academicwritingrecommender.entities.UserRepository;
import com.shitajimado.academicwritingrecommender.entities.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User register(UserDto userDto) throws UserAlreadyExistsException, UserNotExistsException {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            var user = userRepository.save(new User(userDto, new Role("USER")));
            return user; //login(new UserDto(user.getLogin(), user.getPassword(), user.getPassword()));
        }
    }

    public User login(UserDto userDto) throws UserNotExistsException {
        return Optional.of(loadUserByUsername(userDto.getUsername())).flatMap(
                user -> {
                    if (true/*user.checkPassword(userDto.getPassword())*/) {
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                }
        ).orElseThrow(() -> new UserNotExistsException("No such user"));
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException("No such user");
        }

        return user;
    }
}

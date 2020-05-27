package com.shitajimado.academicwritingrecommender.services;

import com.shitajimado.academicwritingrecommender.core.exceptions.UserAlreadyExistsException;
import com.shitajimado.academicwritingrecommender.core.exceptions.UserNotExistsException;
import com.shitajimado.academicwritingrecommender.entities.User;
import com.shitajimado.academicwritingrecommender.entities.UserRepository;
import com.shitajimado.academicwritingrecommender.entities.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User register(UserDto userDto) throws UserAlreadyExistsException {
        if (userRepository.existsByLogin(userDto.getLogin())) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            return userRepository.save(new User(userDto));
        }
    }

    public User login(UserDto userDto) throws UserNotExistsException {
        return Optional.of(userRepository.findByLogin(userDto.getLogin())).flatMap(
                user -> {
                    if (user.checkPassword(userDto.getPassword())) {
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                }
        ).orElseThrow(() -> new UserNotExistsException("No such user"));
    }
}

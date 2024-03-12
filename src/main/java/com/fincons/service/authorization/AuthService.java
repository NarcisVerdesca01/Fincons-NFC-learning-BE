package com.fincons.service.authorization;

import com.fincons.dto.UserDto;
import com.fincons.entity.Role;
import com.fincons.entity.User;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.jwt.LoginDto;
import com.fincons.mapper.UserAndRoleMapper;
import com.fincons.repository.RoleRepository;
import com.fincons.repository.UserRepository;
import com.fincons.utility.EmailValidator;
import com.fincons.utility.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements IAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);


    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;


    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;


    private UserAndRoleMapper userAndRoleMapper;


    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserAndRoleMapper userAndRoleMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAndRoleMapper = userAndRoleMapper;
    }
    
    @Override
    public String registerStudent(UserDto userDto) throws UserDataException {

        String emailDto = userDto.getEmail().toLowerCase().replace(" ", "");
        if (userRepository.existsByEmail(emailDto) || emailDto.isEmpty() || !EmailValidator.isValidEmail(emailDto)) {
            throw new UserDataException(UserDataException.emailInvalidOrExist());
        }
        if (!PasswordValidator.isValidPassword(userDto.getPassword())) {
            throw new UserDataException(UserDataException.passwordDoesNotRespectRegexException());
        }


        User userToSave = userAndRoleMapper.dtoToUser(userDto);
        userToSave.setFirstName(userDto.getFirstName());
        userToSave.setLastName(userDto.getLastName());
        userToSave.setEmail(emailDto);
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userToSave.setBirthDate(userDto.getBirthDate());


        Role role = roleToAssign("ROLE_STUDENT");
        userToSave.setRoles(List.of(role));


       User userSaved = userRepository.save(userToSave);
        if(userRepository.findByEmail(userSaved.getEmail()) == null){
            throw new UserDataException(UserDataException.somethingGoesWrong());
        }
        LOG.info("Student registered: " + userSaved.getEmail());
        return "Student registered successfully";
    }



    @Override
    public String registerTutor(UserDto userDto) throws UserDataException {
        String emailDto = userDto.getEmail().toLowerCase().replace(" ", "");
        if (
                userRepository
                        .existsByEmail(emailDto) ||
                        emailDto.isEmpty() ||
                        !EmailValidator.isValidEmail(emailDto)
        ) {
            throw new UserDataException(UserDataException.emailInvalidOrExist());
        }
        if (!PasswordValidator.isValidPassword(userDto.getPassword())) {
            throw new UserDataException(UserDataException.passwordDoesNotRespectRegexException());
        }

        User userToSave = userAndRoleMapper.dtoToUser(userDto);
        userToSave.setFirstName(userDto.getFirstName());
        userToSave.setLastName(userDto.getLastName());
        userToSave.setEmail(userDto.getEmail().toLowerCase().replace(" ", ""));
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userToSave.setBirthDate(userDto.getBirthDate());//Controllo dell'età ?

        Role role = roleToAssign("ROLE_TUTOR");
        userToSave.setRoles(List.of(role));
        User userSaved = userRepository.save(userToSave);
        if (!userRepository.existsByEmail(userSaved.getEmail())) {
            throw new UserDataException("Something goes wrong!");
        }
        LOG.info("Tutor registered: " + userSaved.getEmail());
        return "Tutor registered successfully";
    }

    @Override
    public String registerAdmin(UserDto userDto) throws UserDataException {
        String emailDto = userDto.getEmail().toLowerCase().replace(" ", "");
        if (userRepository.existsByEmail(emailDto) || emailDto.isEmpty() || !EmailValidator.isValidEmail(emailDto)) {
            throw new UserDataException(UserDataException.emailInvalidOrExist());
        }
        if (!PasswordValidator.isValidPassword(userDto.getPassword())) {
            throw new UserDataException(UserDataException.passwordDoesNotRespectRegexException());
        }
        User userToSave = userAndRoleMapper.dtoToUser(userDto);
        userToSave.setFirstName(userDto.getFirstName());
        userToSave.setLastName(userDto.getLastName());
        userToSave.setEmail(userDto.getEmail().toLowerCase().replace(" ", ""));
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleToAssign("ROLE_ADMIN");

        userToSave.setRoles(List.of(role));

        User userSaved = userRepository.save(userToSave);
        if (!userRepository.existsByEmail(userSaved.getEmail())) {
            throw new UserDataException("Something goes wrong!");
        }
        return "Admin registered successfully";
    }


    @Override
    public User getUserByEmail() {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }

        LOG.info("User info: " +  loggedUser);

        return userRepository.findByEmail(loggedUser);
    }



    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }



    public Role roleToAssign(String nameRole) {
        Role role = roleRepository.findByName(nameRole);
        if (role == null) {
            Role newRole = new Role();
            newRole.setName(nameRole);
            role = roleRepository.save(newRole);
        }
        return role;
    }



}

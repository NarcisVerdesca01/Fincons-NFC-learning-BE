package com.fincons.config;

import com.fincons.entity.Ability;
import com.fincons.entity.Role;
import com.fincons.entity.User;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.RoleRepository;
import com.fincons.repository.UserRepository;
import com.fincons.service.authorization.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AbilityRepository abilityRepository;

    @Override
    public void run(String... args) throws Exception {
        populateAdmin();
        populateTutor();
        populateAbility();
    }


    private void populateAdmin() {

        if (
                userRepository.findByEmail("admin@gmail.com") == null
                        &&
                        roleRepository.findByName("ROLE_ADMIN") == null
        ) {
            User admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("Password!"));
            Role role = authService.roleToAssign("ROLE_ADMIN");
            admin.setRoles(List.of(role));
            User admnSaved = userRepository.save(admin);
        }
    }

    private void populateAbility(){
        if
        (
                (!abilityRepository.existsByNameAndDeletedFalse("Informatica") || abilityRepository.findById(1L).isEmpty())
                        &&
                        (!abilityRepository.existsByNameAndDeletedFalse("Database") || abilityRepository.findById(2L).isEmpty())
                        &&
                        (!abilityRepository.existsByNameAndDeletedFalse("Java") || abilityRepository.findById(3L).isEmpty())
        )
        {
            Ability ability1 = new Ability(1L,"Informatica", null,null,false );
            Ability ability2 = new Ability(2L,"Database", null,null,false );
            Ability ability3 = new Ability(3L,"Java", null,null,false );

            abilityRepository.save(ability1);
            abilityRepository.save(ability2);
            abilityRepository.save(ability3);
        }
    }

    private void populateTutor() {

        if (
                userRepository.findByEmail("tutor@gmail.com") == null
                        &&
                        roleRepository.findByName("ROLE_TUTOR") == null
        ) {
            User tutor = new User();
            tutor.setFirstName("tutor");
            tutor.setLastName("tutor");
            tutor.setEmail("tutor@gmail.com");
            tutor.setPassword(passwordEncoder.encode("Password!"));
            Role role = authService.roleToAssign("ROLE_TUTOR");
            tutor.setRoles(List.of(role));
            User tutorSaved = userRepository.save(tutor);
        }
    }








}

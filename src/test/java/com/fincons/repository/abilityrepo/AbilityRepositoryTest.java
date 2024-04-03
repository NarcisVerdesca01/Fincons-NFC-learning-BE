package com.fincons.repository.abilityrepo;


import com.fincons.entity.Ability;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AbilityRepositoryTest {

    @MockBean
    private AbilityRepository abilityRepository;

    @Autowired
    private AbilityMapper abilityMapper;

    @DisplayName("JUnit test for getAbilityById ")
    @Test
    public void RetrieeveAbility_findByName(){

        Ability ability1 = new Ability(1L,"Informatica",null,null,false);
        Ability ability1Saved = abilityRepository.save(ability1);
        Ability response = abilityRepository.findByName("Informatica");
        List<Ability> listOfAbilities = abilityRepository.findAll();

        when(abilityRepository.findByName("Informatica")).thenReturn(ability1Saved);
    }


}

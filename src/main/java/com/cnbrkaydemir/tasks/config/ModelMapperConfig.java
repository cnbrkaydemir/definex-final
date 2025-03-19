package com.cnbrkaydemir.tasks.config;

import com.cnbrkaydemir.tasks.dto.UserDto;
import com.cnbrkaydemir.tasks.model.Users;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.getConfiguration().setPropertyCondition(context -> {
            if (context.getSource() instanceof Collection) {
                return true;
            }

            return !(context.getSource() instanceof Users &&
                    "authorities".equals(context.getMapping().getLastDestinationProperty().getName()));
        });

        TypeMap<Users, UserDto> typeMap = modelMapper.createTypeMap(Users.class, UserDto.class);
        typeMap.addMappings(mapper -> {
            mapper.map(Users::getId, UserDto::setId);
            mapper.map(Users::getFirstName, UserDto::setFirstName);
            mapper.map(Users::getLastName, UserDto::setLastName);
            mapper.map(Users::getEmail, UserDto::setEmail);
            mapper.map(Users::getPhoneNumber, UserDto::setPhoneNumber);
            mapper.map(Users::getRole, UserDto::setRole);
            // Don't map collections or you can create specific mappings for them
        });

        return  modelMapper;
    }
}

package app.moz.smartdev.service;

import app.moz.smartdev.dtos.RolesDto;
import app.moz.smartdev.entity.Role;
import app.moz.smartdev.repository.RoleRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class RoleServiceImpl implements RoleService{
     private final RoleRepository roleRepository;
     private final ModelMapper modelMapper;

    @Override
    public List<RolesDto> getAllRoles() {

        try {
            List<RolesDto> roles = roleRepository.findAll().stream()
                    .map(role -> modelMapper.map(role, RolesDto.class))
                    .toList();
            return roles;
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }


    }

    @Override
    public RolesDto getRoleById(String id) {
        return null;
    }

    @Override
    public RolesDto createRole(RolesDto role) {

        Role role1 = new Role();

        role1.setName(role.getName());
        role1.setDescription(role.getDescription());

        try {
            Role role2 = roleRepository.save(role1);
            return modelMapper.map(role2, RolesDto.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public RolesDto updateRole(RolesDto role) {
        return null;
    }

    @Override
    public void deleteRole(String id) {

    }
}

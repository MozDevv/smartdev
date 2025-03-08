package app.moz.smartdev.service;

import app.moz.smartdev.dtos.RolesDto;

import java.util.List;

public interface RoleService {

  List<RolesDto> getAllRoles();

    RolesDto getRoleById(String id);

    RolesDto createRole(RolesDto role);

    RolesDto updateRole(RolesDto role);

    void deleteRole(String id);
}

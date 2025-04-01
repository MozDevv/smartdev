package app.moz.smartdev.controller;

import app.moz.smartdev.configs.ResponseBuilder;
import app.moz.smartdev.configs.ResponseWrapper;
import app.moz.smartdev.dtos.RolesDto;
import app.moz.smartdev.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RolesRepository {

    private final RoleService roleService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseWrapper<RolesDto> getAllRoles(
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "10") int pageSize
    ) {

  try{
      List <RolesDto > rolesDtos = roleService.getAllRoles();
      int totalCount = rolesDtos.size();
      int totalPages = (int) Math.ceil((double) totalCount / pageSize);
      return ResponseBuilder.createResponseWrapper(
              rolesDtos,
              pageNumber,
              pageSize,
              true,
              "Roles retrieved successfully",
              200
      );
  } catch (Exception e) {
      return ResponseBuilder.createErrorResponse("Error fetching roles",
              null, e.getMessage(), 500);
    }}

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RolesDto createRole(@RequestBody RolesDto rolesDto) {
        return roleService.createRole(rolesDto);
    }
}

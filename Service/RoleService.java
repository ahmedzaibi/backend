package com.example.demo.Service;

import com.example.demo.entity.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    void deleteRole(Long id);
    Role affectRole(long idRole , long idUser);

    void updateRoleLabel(Long userId, Long roleId, String newModelLabel);
}

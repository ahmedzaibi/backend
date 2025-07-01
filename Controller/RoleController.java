package com.example.demo.Controller;

import com.example.demo.Service.RoleService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

private final RoleService roleService;

    @PutMapping("/{userId}/{roleId}/update-label")
    public ResponseEntity<String> updateRoleLabel(
            @PathVariable Long userId,
            @PathVariable Long roleId,
            @RequestBody Map<String, String> request) {

        String newModelLabel = request.get("modelLabel");
        roleService.updateRoleLabel(userId, roleId, newModelLabel);

        return ResponseEntity.ok(" Main Role successfully for user ID: " + userId);
    }

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @PutMapping("/affecter/{idRole}/{idUser}")
    public ResponseEntity<String> affecterRole(@PathVariable Long idRole,@PathVariable Long idUser){

        roleService.affectRole(idRole, idUser);

        return ResponseEntity.ok(" Affecter Role successfully");
    }
    @PostMapping("/create")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }


}
package com.example.demo.Service;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public void deleteRole(Long id) {roleRepository.deleteById(id);
    }


    @Override
    public Role affectRole(long idRole, long idUser) {

        Role role = roleRepository.findById(idRole).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setUser(userRepository.findByNudoss(idUser));
        roleRepository.save(role);
        userRepository.findByNudoss(idUser).getRoles().add(role);
        return role;
    }
    @Transactional
    public void updateRoleLabel(Long userId, Long roleId, String newModelLabel) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Role role = user.getRoles().stream()
                .filter(r -> r.getDossierID().equals(roleId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Role not found for user with ID: " + roleId));

        role.setModelLabel(newModelLabel);

        roleRepository.save(role);
    }
}
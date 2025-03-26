package com.example.algamoney.api.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.algamoney.model.Role;
import com.example.algamoney.model.Usuario;
import com.example.algamoney.repository.RoleRepository;
import com.example.algamoney.repository.UsuarioRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
	@Autowired
    private RoleRepository roleRepository;
	@Autowired
    private UsuarioRepository usuarioRepository;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @Override
    @Transactional
    public void run(String... args) throws Exception {
    	
    	

        var roleAdmin = roleRepository.findByRoleNome(Role.Values.ADMIN.name());
        
         System.out.println(roleAdmin); 

        
        var userAdmin = usuarioRepository.findByuserName("admin");
        System.out.println(userAdmin);

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                () -> {
                    var user = new Usuario();
                    user.setUserName("admin");
                    user.setSenha(passwordEncoder.encode("123"));
        
                    user.setRoles(Set.of(roleAdmin.get()));
                    usuarioRepository.save(user);
                }
        );
    }
}

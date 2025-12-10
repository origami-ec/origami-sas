package org.ibarra.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig {

    Logger log = Logger.getLogger(this.getClass().getName());

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/**")
                .permitAll().anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        String[][] usersGroupsAndRoles = {
                {"salaboy", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"ryandawsonuk", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"erdemedeiros", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
        };
        List<String> authoritiesStrings = Arrays.asList("ROLE_ACTIVITI_USER", "GROUP_activitiTeam", "GROUP_otherTeam", "ROLE_ACTIVITI_ADMIN");
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//                System.out.println("loadUserByUsername: " + username);
                User user = new User(username, passwordEncoder.encode(username), authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList()));

                return user;
            }
        };
    }

}

package org.sas.zull.security;

import org.sas.zull.conf.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable().authorizeRequests()

                .antMatchers("/autenticar").permitAll()
                .antMatchers("/autenticarAD").permitAll()
                .antMatchers("/validarToken").permitAll()
                .antMatchers("/authenticateDesk").permitAll()

                .antMatchers("/servicios-docs/sas/api/imagen/**").permitAll()
                .antMatchers("/servicios-docs/sas/api/reemplazar/archivo").permitAll()
                .antMatchers("/servicios-planificacion/sas/api/parametro/**").permitAll()

                .antMatchers("/servicios-docs/sas/api/imagen/**").permitAll()
                .antMatchers("/servicios-planificacion/sas/api/parametro/**").permitAll()
                .antMatchers("/servicios-planificacion/sas/api/planTipo/**").permitAll()
                .antMatchers("/servicios-planificacion/sas/api/plan/find/**").permitAll()
                .antMatchers("/servicios-mail/sas/api/enviarCorreo").permitAll()
                .antMatchers("/servicios/sas/api/valor/find").permitAll()
                .antMatchers("/servicios/sas/api/entidad/find").permitAll()
                .antMatchers("/servicios/sas/api/entidad/findAll").permitAll()
                .antMatchers("/servicios/sas/api/usuario/consultar").permitAll()


                .antMatchers("/servicios-archivos/**").permitAll()
                .antMatchers("/servicios/sas/api/documents/pdf/**").permitAll()
                .antMatchers("/servicios/sas/api/documents/grabar").permitAll()

                .antMatchers("/servicios-docs/sas/api/guardar/historico/origamigt").permitAll()

                .antMatchers("/servicios/sas/api/recuperar/usuario").permitAll()
                .antMatchers("/servicios/sas/api/menu/findAll").permitAll()
                .antMatchers("/servicios/sas/api/user/find").permitAll()
                .antMatchers("/servicios/sas/api/generar/codigo/verificacion/**").permitAll()
                .antMatchers("/servicios/sas/api/validar/codigo/verificacion").permitAll()
                .antMatchers("/servicios/sas/api/usuario/actualizar/clave").permitAll()
                .antMatchers("/servicios/sas/api/update/password/user").permitAll()
                .antMatchers("/servicios/sas/api/create/aclLogin").permitAll()
                .antMatchers("/servicios/sas/api/valor/code/**").permitAll()
                .antMatchers("/servicios-logs/**").permitAll()
                .antMatchers("/sas/servicios/**").permitAll()
                .antMatchers("/servicios/**").permitAll()

                .antMatchers("/servicios-firmaec/sas/api/firmaElectronica/documentos/**").permitAll() //PARA QUE SE GUARDEN DESDE LA APP DE ESCRITORIO
                .antMatchers("/servicios-planificacion/sas/api/firmaDocumento/consultarDocumentosXservidor/**").permitAll()
                .antMatchers("/servicios-administrativo/sas/api/firmaDocumento/consultarDocumentosXservidor/**").permitAll()

                .anyRequest().authenticated().and()
                .httpBasic().realmName("OrigamiGT").and().requestCache().requestCache(new NullRequestCache());
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}

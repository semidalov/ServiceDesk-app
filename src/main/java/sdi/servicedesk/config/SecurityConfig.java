package sdi.servicedesk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import sdi.servicedesk.services.UserDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, AuthenticationSuccessHandler authenticationSuccessHandler, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new EncodingFilterConfig(), ChannelProcessingFilter.class)
                    .authorizeRequests()
                    .antMatchers("/auth/login").permitAll()
                    .antMatchers("/tasks",
                            "/tasks/rows_count",
                            "/tasks/get_all",
                            "/tasks/get_attached",
                            "/tasks/get_mine",
                            "/tasks/get_opened",
                            "/tasks/new",
                            "/tasks/save",
                            "/tasks/{id}",
                            "/filter/tasks",
                            "/filter/equipment",
                            "/equipment/rows_count",
                            "/equipment/get_equipment",
                            "/incidents/rows_count/",
                            "/incidents/incidents",
                            "/static/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().hasRole("ADMIN")
                .and()
                    .formLogin().loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
//                    .defaultSuccessUrl("/tasks", true)
                    .failureUrl("/auth/login?error")
                    .successHandler(authenticationSuccessHandler)
                .and()
                    .logout()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/auth/login");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}

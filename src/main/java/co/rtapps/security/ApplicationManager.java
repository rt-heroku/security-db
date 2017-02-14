package co.rtapps.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import co.rtapps.security.entities.Role;
import co.rtapps.security.entities.UserAccount;
import co.rtapps.security.repositories.UserRepository;
import co.rtapps.security.repositories.UserRolesRepository;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
public class ApplicationManager {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

	private static final String SYSADMIN = "sysadmin";

	public static void main(String[] args) {
        SpringApplication.run(ApplicationManager.class, args);
    }
    
    @Autowired
    UserDetailsService customerUserDetailsService;
    
    @Autowired 
    UserRolesRepository userRolesRepository;
    
    @Bean
    CommandLineRunner init(final UserRepository userRepository) {
      
      return new CommandLineRunner() {

		@Override
        public void run(String... arg0) throws Exception {
    		
    		for (int i = 0; i< arg0.length; i++)
    			System.out.println("Running with Argument [" + i + "] =" + arg0[i] );

        	if (arg0.length == 2){
	        	System.out.println("EXECUTING ApplicationManager this will create or replace the SUPER USER " + SYSADMIN + " !!!! - ");
	        	String password = arg0[0];
	        	String email = arg0[1];
	        	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    		String hashedPassword = passwordEncoder.encode(password);
	    		
	    		UserAccount u = userRepository.findByUsername(SYSADMIN);
        		Set<Role> roles = new HashSet<Role>();
    			Role r = userRolesRepository.findByName(ROLE_ADMIN);
    			if (r == null)
    				r = new Role(ROLE_ADMIN);
    			roles.add(r);
                
	    		if (u == null){
	    			u = new UserAccount(SYSADMIN, hashedPassword, email,roles);
	    		}else{
	    			u.setEmail(email);
	    			u.setPassword(hashedPassword);
	    			u.setEnabled(1);
	    			u.setRoles(roles);
	    		}
	    		
	    		userRepository.save(u);
	        	
	        	System.out.println("\n\nUser " + SYSADMIN + " has been reset!\n\n");
	        	System.out.println("Press CTRL+C to end application...\n\n");
        	}
        }
        
      };

    }
    
}
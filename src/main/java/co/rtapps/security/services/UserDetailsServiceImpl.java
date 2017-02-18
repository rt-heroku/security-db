package co.rtapps.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.rtapps.security.entities.Role;
import co.rtapps.security.entities.UserAccount;
import co.rtapps.security.entities.UserInputDto;
import co.rtapps.security.repositories.UserRepository;
import co.rtapps.security.repositories.UserRolesRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserRolesRepository userRolesRepository;

	public UserAccount findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Transactional
	public void save(UserAccount user) {
		Set<Role> newRoles = new HashSet<Role>();

		for (Role r : user.getRoles()) {
			Role nr = userRolesRepository.findByName(r.getName());
			if (nr == null)
				nr = userRolesRepository.save(r);

			newRoles.add(nr);
		}
		user.setRoles(newRoles);

		userRepository.save(user);
	}


	public List<Role> findRolesByUsername(String username) {
		return userRolesRepository.findUserRolesByUserName(username);
	}
	
	public void remove(String username){
		userRepository.delete(findByUsername(username));
	}
	
	public Role findRoleByName(String role){
		return userRolesRepository.findByName(role);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount user = userRepository.findByUsername(username);

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (Role role : user.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				grantedAuthorities);
	}

	public UserDetails addUser(UserInputDto user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(user.getPassword());

		UserAccount u = findByUsername(user.getUsername());
		Set<Role> roles = new HashSet<Role>();
		for (String s : user.getRoles()){
			Role r = userRolesRepository.findByName(s);
			if (r == null)
				r = new Role(s);
			roles.add(r);
		}
		
		if (u == null) {
			u = new UserAccount(user.getUsername(), hashedPassword, user.getEmail(), roles);
		} else {
			u.setEmail(user.getEmail());
			u.setPassword(hashedPassword);
			u.setEnabled(1);
			u.setRoles(roles);
		}

		save(u);
		return loadUserByUsername(user.getUsername());
	}

}

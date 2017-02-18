package co.rtapps.security.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserAccount {
	private Long id;
	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "enabled")
	private int enabled;
	private Set<Role> roles;

	public UserAccount() {

	}

	public UserAccount(String username, String password, String email, Set<Role> roles) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = 1;
		this.roles = roles;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToMany(cascade = { CascadeType.ALL })
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {

		if (!roles.contains(role)) {
			roles.add(role);
			role.addUser(this);
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public void removeRole(Role role) {
		if (roles.contains(role)) {
			roles.remove(role);

			role.removeUser(this);
		}

	}
}

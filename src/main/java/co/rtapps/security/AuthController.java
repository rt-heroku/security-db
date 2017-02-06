package co.rtapps.security;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heroku.security.entities.UserInputDto;
import com.heroku.security.services.SecurityService;
import com.heroku.security.services.UserDetailsServiceImpl;
import com.heroku.security.validators.UserValidator;

@RestController
@RequestMapping("/user")
public class AuthController {

	@Autowired
	private UserDetailsServiceImpl userService;

	@Autowired
	private SecurityService securityService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(new UserValidator());
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> doLogin(@RequestParam String username, @RequestParam String password) {
		
		securityService.autologin(username, password);
		
		return new ResponseEntity<Map<String, Object>>(getLoggedUserAuthDetails(), HttpStatus.OK);
	}

	@RequestMapping(value = "/whoami", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> whoami() {

		return new ResponseEntity<Map<String, Object>>(getLoggedUserAuthDetails(), HttpStatus.OK);
	}

	private Map<String, Object> getLoggedUserAuthDetails() {
		Map<String, Object> ret = new HashMap<String, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		ret.put("user", auth.getPrincipal());
		ret.put("auth_details", auth.getDetails());
		return ret;
	}

	@RequestMapping(value = "/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDetails> getUser(@RequestParam String username) {

		UserDetails ua = userService.loadUserByUsername(username);

		return new ResponseEntity<UserDetails>(ua, HttpStatus.OK);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<UserInputDto> add(@Valid @RequestBody UserInputDto user) {
		UserDetails ud = userService.addUser(user);
		user.setUsername(ud.getUsername());
		user.setPassword("[RESTRICTED]");
		user.setPasswordConfirm("[RESTRICTED]");
		return new ResponseEntity<UserInputDto>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/remove/{username}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> remove(@PathVariable String username) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName().toLowerCase().equals(username.toLowerCase()))
			throw new RuntimeException("Unable to delete myself!");
		
		userService.remove(username);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
}

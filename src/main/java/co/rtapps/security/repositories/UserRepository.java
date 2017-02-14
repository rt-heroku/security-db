package co.rtapps.security.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.rtapps.security.entities.UserAccount;

@Repository
public interface UserRepository extends CrudRepository<UserAccount, Long> {
    public UserAccount findByUsername(String username);
    
}
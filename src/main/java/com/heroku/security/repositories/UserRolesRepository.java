package com.heroku.security.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.heroku.security.entities.Role;

@Repository
public interface UserRolesRepository extends CrudRepository<Role, Long> {
	
	public Role findById(Long id);
	
	public Role findByName(String name);
	
	public List<Role> findByNameIn(List<String> names);
	
	@Query("select a.name from Role a, UserAccount b where b.username=?1 and a.id=b.id")
    public List<String> findRoleByUserName(String username);

	@Query("select a from Role a, UserAccount b where b.username=?1 and a.id=b.id")
	public List<Role> findUserRolesByUserName(String username);
	
}
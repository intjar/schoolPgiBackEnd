package com.np.schoolpgi.dao;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer>{

	Optional<Roles> findByNameIgnoreCase(String name);

	Optional<Roles> findByCode(Integer code);

	Optional<Roles> findByNameIgnoreCaseAndIdNot(String name, Integer id);

	Optional<Roles> findByCodeAndIdNot(@NotNull Integer code, Integer id);

}

package com.np.schoolpgi.dao;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer>{
	Login findByUsername(String username);
	Login findByUid(Integer uid);
	Login findByUsernameAndUidNot(String username, @NotNull Integer id);
}

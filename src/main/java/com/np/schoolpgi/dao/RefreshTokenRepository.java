package com.np.schoolpgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.RefreshToken;
import com.np.schoolpgi.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

	RefreshToken findByToken(String token);

	int deleteByUser(User user);

}

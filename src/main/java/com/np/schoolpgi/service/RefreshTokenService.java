package com.np.schoolpgi.service;

import com.np.schoolpgi.model.RefreshToken;

public interface RefreshTokenService {
	RefreshToken createRefreshToken(Integer userId);
	RefreshToken verifyExpiration(RefreshToken token);
	RefreshToken findByToken(String token);
}

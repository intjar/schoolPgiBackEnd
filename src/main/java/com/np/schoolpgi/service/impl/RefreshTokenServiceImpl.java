package com.np.schoolpgi.service.impl;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.RefreshTokenRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.exception.TokenRefreshException;
import com.np.schoolpgi.model.RefreshToken;
import com.np.schoolpgi.service.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	private static final Logger LOGGER = LogManager.getLogger(RefreshTokenServiceImpl.class);
	
	@Value("${np.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public RefreshToken findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public RefreshToken createRefreshToken(Integer userId) {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setUser(userRepository.findByUserId(userId));
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			LOGGER.error(token.getToken()+" :: "+
					APPServiceCode.APP170.getStatusDesc());
			throw new TokenRefreshException(token.getToken(),
					APPServiceCode.APP170.getStatusDesc());
		}

		return token;
	}

	@Transactional
	public int deleteByUserId(Integer userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}

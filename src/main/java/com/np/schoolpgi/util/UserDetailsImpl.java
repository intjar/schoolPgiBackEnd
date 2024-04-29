package com.np.schoolpgi.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.np.schoolpgi.model.Login;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	@JsonIgnore
	private String password;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//return user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getRole_id().toString()))
//        .collect(Collectors.toList());
		return null;
	}
	public UserDetailsImpl(Login login) {
		super();
		this.id = login.getId();
		this.username = login.getUsername();
		this.password = login.getPassword();
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
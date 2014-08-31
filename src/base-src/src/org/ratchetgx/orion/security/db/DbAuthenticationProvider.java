/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ratchetgx.orion.security.db;

import com.wiscom.is.IdentityManager;
import com.wiscom.is.SSOToken;

import org.apache.commons.lang.StringUtils;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 
 * @author hrfan
 */
public class DbAuthenticationProvider implements AuthenticationProvider,
		InitializingBean, MessageSourceAware {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private MessageSource messageSource;

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (!((authentication instanceof UsernamePasswordAuthenticationToken) && authentication
				.getPrincipal().equals(DbAuthenticationFilter.DB_IDENTIFIER))) {
			return null;
		}

		String principal = (String) authentication.getPrincipal();
 
		UsernamePasswordAuthenticationToken authRequest = (UsernamePasswordAuthenticationToken) authentication;
		String[] credentials = (String[]) authRequest.getCredentials();

		try {
			UserDetails loadedUser = retrieveUser(credentials[0],
					DbAuthenticationFilter.DB_IDENTIFIER);

			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
					loadedUser, credentials[1], loadedUser.getAuthorities());

			result.setDetails(authentication.getDetails());

			return result;
		} catch (Exception ex) {
			throw new DbAuthenticationException(ex.getMessage());
		}
	}

	public boolean supports(Class<?> authentication) {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private UserDetails retrieveUser(String username, String userLoginType)
			throws AuthenticationException {
		UserDetails loadedUser;

		try {
			loadedUser = userDetailsService.loadUserByUsername(username);
			if (loadedUser instanceof SsfwUserDetails) {
				((SsfwUserDetails) loadedUser).setUserLoginType(userLoginType);
			}
		} catch (UsernameNotFoundException notFound) {
			log.error("", notFound);
			throw notFound;
		} catch (Exception repositoryProblem) {
			throw new AuthenticationServiceException(
					repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new DbAuthenticationException("未找到相应的用户信息");
		}

		return loadedUser;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	private UserDetailsService userDetailsService;
}

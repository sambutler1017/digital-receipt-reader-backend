package com.digital.receipt.jwt.utility;

import javax.servlet.http.HttpServletRequest;

import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

/**
 * JwtHolder class to get common information from token
 * 
 * @author Sam Butler
 * @since 8/04/2020
 */
@Service("JwtHolder")
public class JwtHolder {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtHolder.class);

	private JwtParser jwtParser;

	private ActiveProfile activeProfile;

	public JwtHolder(ActiveProfile profile) {
		activeProfile = profile;
		jwtParser = Jwts.parser().setSigningKey(activeProfile.getSigningKey());
	}

	/**
	 * Get the current userId from the request headers token
	 * 
	 * @return int of the userId from the current token
	 */
	public int getRequiredUserId() {
		try {
			return Integer.parseInt(jwtParser.parseClaimsJws(getToken()).getBody().get("userId").toString());
		} catch (Exception e) {
			LOGGER.debug("Invalid or no Token given.");
			return -1;
		}
	}

	/**
	 * Get the userId from the passed in token
	 * 
	 * @param token - String of the token to decode
	 * @return int of the userId from the current token
	 */
	public int getRequiredUserId(String token) {
		try {
			return Integer.parseInt(jwtParser.parseClaimsJws(token).getBody().get("userId").toString());
		} catch (Exception e) {
			LOGGER.debug("Invalid or no Token given.");
			return -1;
		}
	}

	/**
	 * Get the current email from the request headers token
	 * 
	 * @return String of the email from the current token
	 */
	public String getRequiredEmail() {
		try {
			return jwtParser.parseClaimsJws(getToken()).getBody().get("email").toString();
		} catch (Exception e) {
			LOGGER.debug("Invalid or no Token given.");
			return "";
		}
	}

	/**
	 * Get the email from the passed in token
	 * 
	 * @param token - String of the token to decode
	 * @return String of the email from the current token
	 */
	public String getRequiredEmail(String token) {
		try {
			return jwtParser.parseClaimsJws(token).getBody().get("email").toString();
		} catch (Exception e) {
			LOGGER.debug("Invalid or no Token given.");
			return "";
		}
	}

	/**
	 * Get the current webRole from the request headers token
	 * 
	 * @return String of the webRole from the current token
	 */
	public WebRole getWebRole() {
		try {
			return WebRole.valueOf(jwtParser.parseClaimsJws(getToken()).getBody().get("webRole").toString());
		} catch (Exception e) {
			LOGGER.debug("Invalid or no Token given.");
			return WebRole.USER;
		}
	}

	/**
	 * Get the webRole from the passed in token
	 * 
	 * @param token - String of the token to decode
	 * @return String of the webRole from the current token
	 */
	public WebRole getWebRole(String token) {
		try {
			return WebRole.valueOf(jwtParser.parseClaimsJws(token).getBody().get("webRole").toString());
		} catch (Exception e) {
			LOGGER.debug("Invalid or no Token given.");
			return WebRole.USER;
		}
	}

	/**
	 * Get the current token passed in with the request
	 * 
	 * @return String of the token from the request headers
	 */
	public String getToken() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		return request.getHeader("Authorization") == null ? null : request.getHeader("Authorization").split(" ")[1];
	}

	/**
	 * Gets the type of call that was made on the request.
	 * 
	 * @return {@link String} of the call type
	 */
	public String getCallType() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		return request.getHeader("callType") == null ? "" : request.getHeader("callType");
	}
}

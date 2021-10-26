package com.digital.receipt.jwt.config;

import static com.digital.receipt.jwt.config.JwtGlobals.VOID_ENDPOINTS;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.digital.receipt.common.enums.Environment;
import com.digital.receipt.common.exceptions.BaseException;
import com.digital.receipt.jwt.utility.JwtTokenUtil;
import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JWT token validator class
 *
 * @author Sam butler
 * @since Dec 5, 2020
 */
@Component
public class JwtTokenValidator {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ActiveProfile activeProfile;

    /**
     * Checks to see if the token on the request is valid. If it is not valid then
     * it wil throw an exception, otherwise it wil continue.
     *
     * @param request - The request that is being made to the endpint
     * @return boolean saying if it is a valid token or not
     * @throws IOException
     */
    public void isValidJwt(HttpServletRequest request) throws IOException {
        final String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null && tokenHeader.startsWith("Bearer: ")) {
            String jwtToken = tokenHeader.substring(7);

            isCorrectEnvironment(jwtToken);
            hasCorrectFields(jwtToken);

        } else {
            doesTokenExist(tokenHeader);
        }
    }

    /**
     * Checks to see if the endpoint that was called is a void endpoint or not.
     *
     * @param URI  - URI of the endpoint that was called
     * @param type - What type of request was made (GET, POST, etc.)
     * @return boolean based on if it is a void endpoint or not
     */
    public boolean isVoidEndpoint(String URI, String type) {
        for (Map.Entry<String, List<String>> entry : VOID_ENDPOINTS.entrySet()) {
            if (entry.getKey().equals(URI)) {
                for (String endpointType : entry.getValue()) {
                    if (endpointType.equals("ANY")) {
                        return true;
                    }
                    if (endpointType.equals(type)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks to see if a token exists or if the token does not contain the bearer
     * keyword.
     *
     * @param tokenHeader Header to of the token.
     * @throws BaseException Throws exception based on status of token.
     */
    private void doesTokenExist(String tokenHeader) throws BaseException {
        if (tokenHeader != null) {
            throw new BaseException("JWT Token does not begin with 'Bearer:'");
        } else {
            throw new BaseException("Missing JWT Token.");
        }
    }

    /**
     * Checks to see if it has the required fields on the token.
     *
     * @param token - Token to confirm field claims on
     * @throws IOException Throws exception if it can't read the fields or if it is
     *                     an invalid token.
     */
    private void hasCorrectFields(String token) throws IOException {
        try {
            jwtTokenUtil.getIdFromToken(token);
            jwtTokenUtil.getExpirationDateFromToken(token);
            jwtTokenUtil.getAllClaimsFromToken(token);
        } catch (Exception e) {
            throw new BaseException("Could not process JWT token.");
        }
    }

    /**
     * Validates that the environemnt is correct.
     *
     * @param token to be parsed
     * @throws BaseException
     */
    private void isCorrectEnvironment(String token) throws BaseException {
        Environment environment = Environment.valueOf(jwtTokenUtil.getAllClaimsFromToken(token).get("env").toString());

        if (!activeProfile.getEnvironment().equals(environment)) {
            throw new BaseException("JWT token doesn't match accessing environment!");
        }
    }
}

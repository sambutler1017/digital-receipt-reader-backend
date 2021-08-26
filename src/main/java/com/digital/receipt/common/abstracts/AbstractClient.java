package com.digital.receipt.common.abstracts;

import com.digital.receipt.jwt.utility.JwtHolder;
import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

/**
 * Abstract class for setting up the clients.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Service
public abstract class AbstractClient {

    private String url;

    @Autowired
    private JwtHolder jwtHolder;

    /**
     * Sets the default url path based on the environment.
     * 
     * @param endpointUrl The base endpoint of the clients controller.
     */
    public AbstractClient(String endpointUrl, ActiveProfile activeProfile) {
        Assert.notNull(endpointUrl, "Endpoint Url is required");
        Assert.notNull(activeProfile, "Active Profile is required");
        this.url = String.format("%s/%s", activeProfile.getUriPath(), endpointUrl);
    }

    /**
     * Make get Client request to the given endpoint with the given params.
     * 
     * @param url    The url to be called.
     * @param params Any params needing to be added to the request.
     * @return {@link ResponseSpec} of for the given endpoint.
     */
    public ResponseSpec get(String url, Object... params) {
        return completeRequest(getWebClient().get(), url, params).retrieve();

    }

    /**
     * Make post Client request to the given endpoint with the given params. Will
     * also pass in the body with the request.
     * 
     * @param url    The url to be called.
     * @param params Any params needing to be added to the request.
     * @return {@link ResponseSpec} of for the given endpoint.
     */
    public ResponseSpec post(String url, Object body, Object... params) {
        return completeRequestWithBody(getWebClient().post(), url, body, params).retrieve();

    }

    /**
     * Make put Client request to the given endpoint with the given params. Will
     * also pass in the body with the request.
     * 
     * @param url    The url to be called.
     * @param params Any params needing to be added to the request.
     * @return {@link ResponseSpec} of for the given endpoint.
     */
    public ResponseSpec put(String url, Object body, Object... params) {
        return completeRequestWithBody(getWebClient().put(), url, body, params).retrieve();
    }

    /**
     * Make delete Client request to the given endpoint with the given params.
     * 
     * @param url    The url to be called.
     * @param params Any params needing to be added to the request.
     * @return {@link ResponseSpec} of for the given endpoint.
     */
    public ResponseSpec delete(String url, Object... params) {
        return completeRequest(getWebClient().delete(), url, params).retrieve();

    }

    /**
     * Complete the rquest by adding default headers and creating uri path.
     * 
     * @param requestHeadersUriSpec The type of request to make (get, post, update,
     *                              delete).
     * @param url                   The url to be called.
     * @param params                Any params needing to be added to the request.
     * @return The {@link RequestHeadersSpec} with the given data.
     */
    private RequestHeadersSpec<?> completeRequest(RequestHeadersUriSpec<?> requestHeadersUriSpec, String url,
            Object... params) {
        return requestHeadersUriSpec.uri(url, params)
                .header("Authorization", String.format("Bearer: %s", jwtHolder.getToken()))
                .accept(MediaType.APPLICATION_JSON);
    }

    /**
     * Complete the rquest by adding default headers and creating uri path.
     * 
     * @param requestBodyUriSpec The type of request to make (get, post, update,
     *                           delete).
     * @param url                The url to be called.
     * @param body               The {@link Object} body to send with the request.
     * @param params             Any params needing to be added to the request.
     * @return The {@link RequestHeadersSpec} with the given data.
     */
    private RequestHeadersSpec<?> completeRequestWithBody(RequestBodyUriSpec requestBodyUriSpec, String url,
            Object body, Object... params) {
        return requestBodyUriSpec.uri(url, params)
                .header("Authorization", String.format("Bearer: %s", jwtHolder.getToken()))
                .accept(MediaType.APPLICATION_JSON).bodyValue(body);
    }

    /**
     * Creates the base webclient builder.
     * 
     * @return {@link WebClient.Builder} object with the base url.
     */
    private WebClient getWebClient() {
        return WebClient.builder().baseUrl(url).build();
    }
}

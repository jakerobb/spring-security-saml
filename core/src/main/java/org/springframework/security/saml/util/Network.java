/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.springframework.security.saml.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.saml.SamlKeyException;
import org.springframework.web.client.RestTemplate;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class Network {

	private int connectTimeoutMillis = 5000;
	private int readTimeoutMillis = 10000;

	public byte[] get(String url, boolean skipSslValidation) {
		RestTemplate template = new RestTemplate(createRequestFactory(skipSslValidation));
		return template.getForObject(url, byte[].class);
	}

	public ClientHttpRequestFactory createRequestFactory(boolean skipSslValidation) {
		return createRequestFactory(getClientBuilder(skipSslValidation));
	}

	protected ClientHttpRequestFactory createRequestFactory(HttpClientBuilder builder) {
		return new HttpComponentsClientHttpRequestFactory(builder.build());
	}

	protected HttpClientBuilder getClientBuilder(boolean skipSslValidation) {
		HttpClientBuilder builder = HttpClients.custom()
			.useSystemProperties()
			.setRedirectStrategy(new DefaultRedirectStrategy());
		if (skipSslValidation) {
			builder.setSslcontext(getNonValidatingSslContext());
		}
		builder.setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE);
		RequestConfig config = RequestConfig.custom()
			.setConnectTimeout(getConnectTimeoutMillis())
			.setConnectionRequestTimeout(getConnectTimeoutMillis())
			.setSocketTimeout(getReadTimeoutMillis())
			.build();
		builder.setDefaultRequestConfig(config);
		return builder;
	}

	protected SSLContext getNonValidatingSslContext() {
		try {
			return new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		} catch (KeyManagementException e) {
			throw new SamlKeyException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new SamlKeyException(e);
		} catch (KeyStoreException e) {
			throw new SamlKeyException(e);
		}
	}

	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public Network setConnectTimeoutMillis(int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
		return this;
	}

	public int getReadTimeoutMillis() {
		return readTimeoutMillis;
	}

	public Network setReadTimeoutMillis(int readTimeoutMillis) {
		this.readTimeoutMillis = readTimeoutMillis;
		return this;
	}
}

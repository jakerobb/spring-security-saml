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

package org.springframework.security.saml.saml2.authentication;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.saml.key.SimpleKey;
import org.springframework.security.saml.saml2.signature.AlgorithmMethod;
import org.springframework.security.saml.saml2.signature.DigestMethod;

/**
 * Implementation samlp:ResponseType as defined by
 * https://www.oasis-open.org/committees/download.php/35711/sstc-saml-core-errata-2.0-wd-06-diff.pdf
 * Page 47, Line 1995
 */
public class Response extends StatusResponse<Response> {
	private List<Assertion> assertions = new LinkedList<>();

	private SimpleKey signingKey = null;
	private AlgorithmMethod algorithm;
	private DigestMethod digest;

	public List<Assertion> getAssertions() {
		return Collections.unmodifiableList(assertions);
	}

	public Response setAssertions(List<Assertion> assertions) {
		this.assertions.clear();
		this.assertions.addAll(assertions);
		return this;
	}

	public Response setSigningKey(SimpleKey signingKey,
								  AlgorithmMethod algorithm,
								  DigestMethod digest) {
		this.signingKey = signingKey;
		this.algorithm = algorithm;
		this.digest = digest;
		return this;
	}

	public SimpleKey getSigningKey() {
		return signingKey;
	}

	public AlgorithmMethod getAlgorithm() {
		return algorithm;
	}

	public DigestMethod getDigest() {
		return digest;
	}

	public Response addAssertion(Assertion assertion) {
		this.assertions.add(assertion);
		return this;
	}
}

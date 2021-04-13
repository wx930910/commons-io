/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test {@link ProxyReader}.
 *
 */
public class ProxyReaderTest {

	public static ProxyReader mockProxyReader1(final Reader proxy) {
		ProxyReader mockInstance = mock(ProxyReader.class,
				withSettings().useConstructor(proxy).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		return mockInstance;
	}

	@Test
	public void testNullCharArray() throws Exception {

		final ProxyReader proxy = ProxyReaderTest.mockProxyReader1(new CustomNullReader(0));
		proxy.read((char[]) null);
		proxy.read(null, 0, 0);
		proxy.close();
	}

	@Test
	public void testNullCharBuffer() throws Exception {

		final ProxyReader proxy = ProxyReaderTest.mockProxyReader1(new CustomNullReader(0));
		proxy.read((CharBuffer) null);
		proxy.close();
	}

	/** Custom NullReader implementation */
	private static class CustomNullReader extends NullReader {
		CustomNullReader(final int len) {
			super(len);
		}

		@Override
		public int read(final char[] chars) throws IOException {
			return chars == null ? 0 : super.read(chars);
		}

		@Override
		public int read(final CharBuffer target) throws IOException {
			return target == null ? 0 : super.read(target);
		}
	}
}

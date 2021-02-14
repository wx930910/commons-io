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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MarkShieldInputStreamTest {

	@Test
	public void markIsNoOpWhenUnderlyingDoesNotSupport() throws IOException {
		try (final ProxyInputStream in = mock(ProxyInputStream.class,
				withSettings().useConstructor(new NullInputStream(64, false, false))
						.defaultAnswer(Mockito.CALLS_REAL_METHODS));
				final MarkShieldInputStream msis = new MarkShieldInputStream(in)) {

			msis.mark(1024);
			int[] inReadlimit = new int[1];
			doAnswer((stubInvo) -> {
				int readlimit = stubInvo.getArgument(0);
				inReadlimit[0] = readlimit;
				stubInvo.callRealMethod();
				return null;
			}).when(in).mark(anyInt());

			verify(in, times(0)).mark(anyInt());
			assertEquals(0, inReadlimit[0]);
		}
	}

	@Test
	public void markIsNoOpWhenUnderlyingSupports() throws IOException {
		try (final ProxyInputStream in = mock(ProxyInputStream.class,
				withSettings().useConstructor(new NullInputStream(64, true, false))
						.defaultAnswer(Mockito.CALLS_REAL_METHODS));
				final MarkShieldInputStream msis = new MarkShieldInputStream(in)) {

			msis.mark(1024);
			int[] inReadlimit = new int[1];
			doAnswer((stubInvo) -> {
				int readlimit = stubInvo.getArgument(0);
				inReadlimit[0] = readlimit;
				stubInvo.callRealMethod();
				return null;
			}).when(in).mark(anyInt());

			verify(in, times(0)).mark(anyInt());
			assertEquals(0, inReadlimit[0]);
		}
	}

	@Test
	public void markSupportedIsFalseWhenUnderlyingFalse() throws IOException {
		// test wrapping an underlying stream which does NOT support marking
		try (final InputStream is = new NullInputStream(64, false, false)) {
			assertFalse(is.markSupported());

			try (final MarkShieldInputStream msis = new MarkShieldInputStream(is)) {
				assertFalse(msis.markSupported());
			}
		}
	}

	@Test
	public void markSupportedIsFalseWhenUnderlyingTrue() throws IOException {
		// test wrapping an underlying stream which supports marking
		try (final InputStream is = new NullInputStream(64, true, false)) {
			assertTrue(is.markSupported());

			try (final MarkShieldInputStream msis = new MarkShieldInputStream(is)) {
				assertFalse(msis.markSupported());
			}
		}
	}

	@Test
	public void resetThrowsExceptionWhenUnderylingDoesNotSupport() throws IOException {
		// test wrapping an underlying stream which does NOT support marking
		try (final MarkShieldInputStream msis = new MarkShieldInputStream(new NullInputStream(64, false, false))) {
			assertThrows(UnsupportedOperationException.class, () -> msis.reset());
		}
	}

	@Test
	public void resetThrowsExceptionWhenUnderylingSupports() throws IOException {
		// test wrapping an underlying stream which supports marking
		try (final MarkShieldInputStream msis = new MarkShieldInputStream(new NullInputStream(64, true, false))) {
			assertThrows(UnsupportedOperationException.class, () -> msis.reset());
		}
	}
}

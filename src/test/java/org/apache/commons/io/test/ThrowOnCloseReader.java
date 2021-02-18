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
package org.apache.commons.io.test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.input.NullReader;
import org.apache.commons.io.input.ProxyReader;
import org.mockito.Mockito;

/**
 * Helper class for checking behavior of IO classes.
 */
public class ThrowOnCloseReader {

	public static ProxyReader mockProxyReader1() throws IOException {
		ProxyReader mockInstance = mock(ProxyReader.class,
				withSettings().useConstructor(new NullReader()).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		doThrow(new IOException(mockInstance.getClass().getSimpleName() + ".close() called.")).when(mockInstance)
				.close();
		return mockInstance;
	}

	public static ProxyReader mockProxyReader2(final Reader proxy) throws IOException {
		ProxyReader mockInstance = mock(ProxyReader.class,
				withSettings().useConstructor(proxy).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		doThrow(new IOException(mockInstance.getClass().getSimpleName() + ".close() called.")).when(mockInstance)
				.close();
		return mockInstance;
	}

}

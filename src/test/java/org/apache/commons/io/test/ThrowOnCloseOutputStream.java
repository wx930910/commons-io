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
import static org.mockito.Mockito.spy;

import java.io.IOException;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.ProxyOutputStream;

/**
 * Helper class for checking behavior of IO classes.
 */
public class ThrowOnCloseOutputStream {

	public static ProxyOutputStream mockProxyOutputStream1() throws IOException {
		ProxyOutputStream mockInstance = spy(new ProxyOutputStream(NullOutputStream.NULL_OUTPUT_STREAM));
		doThrow(new IOException(mockInstance.getClass().getSimpleName() + ".close() called.")).when(mockInstance)
				.close();
		return mockInstance;
	}

}

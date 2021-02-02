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

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class MarkShieldInputStreamTest {

	@Test
	public void markIsNoOpWhenUnderlyingSupports() throws IOException {
		try (final MarkTestableInputStream in = new MarkTestableInputStream(new NullInputStream(64, true, false));
				final MarkShieldInputStream msis = new MarkShieldInputStream(in)) {

			msis.mark(1024);

			assertEquals(0, in.markcount);
			assertEquals(0, in.readlimit);
		}
	}

	private static class MarkTestableInputStream extends ProxyInputStream {
		int markcount;
		int readlimit;

		public MarkTestableInputStream(final InputStream in) {
			super(in);
		}

		@SuppressWarnings("sync-override")
		@Override
		public void mark(final int readlimit) {
			// record that `mark` was called
			markcount++;
			this.readlimit = readlimit;

			// invoke on super
			super.mark(readlimit);
		}
	}
}

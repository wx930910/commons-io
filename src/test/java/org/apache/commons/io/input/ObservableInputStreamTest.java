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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.junit.jupiter.api.Test;

public class ObservableInputStreamTest {
	/**
	 * Tests, that {@link Observer#data(int)} is called.
	 */
	@Test
	public void testDataByteCalled() throws Exception, IOException {
		final byte[] buffer = MessageDigestCalculatingInputStreamTest.generateRandomByteStream(4096);
		final ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(buffer));
		final Observer lko = spy(Observer.class);
		boolean[] lkoClosed = new boolean[1];
		boolean[] lkoFinished = new boolean[1];
		int[] lkoLastByteSeen = new int[] { -1 };
		doAnswer((stubInvo) -> {
			int pByte = stubInvo.getArgument(0);
			stubInvo.callRealMethod();
			lkoLastByteSeen[0] = pByte;
			return null;
		}).when(lko).data(anyInt());
		doAnswer((stubInvo) -> {
			stubInvo.callRealMethod();
			lkoClosed[0] = true;
			return null;
		}).when(lko).closed();
		doAnswer((stubInvo) -> {
			stubInvo.callRealMethod();
			lkoFinished[0] = true;
			return null;
		}).when(lko).finished();
		assertEquals(-1, lkoLastByteSeen[0]);
		ois.read();
		assertEquals(-1, lkoLastByteSeen[0]);
		assertFalse(lkoFinished[0]);
		assertFalse(lkoClosed[0]);
		ois.add(lko);
		for (int i = 1; i < buffer.length; i++) {
			final int result = ois.read();
			assertEquals((byte) result, buffer[i]);
			assertEquals(result, lkoLastByteSeen[0]);
			assertFalse(lkoFinished[0]);
			assertFalse(lkoClosed[0]);
		}
		final int result = ois.read();
		assertEquals(-1, result);
		assertTrue(lkoFinished[0]);
		assertFalse(lkoClosed[0]);
		ois.close();
		assertTrue(lkoFinished[0]);
		assertTrue(lkoClosed[0]);
	}

	/**
	 * Tests, that {@link Observer#data(byte[],int,int)} is called.
	 */
	@Test
	public void testDataBytesCalled() throws Exception, IOException {
		final byte[] buffer = MessageDigestCalculatingInputStreamTest.generateRandomByteStream(4096);
		final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		final ObservableInputStream ois = new ObservableInputStream(bais);
		final Observer lko = spy(Observer.class);
		int[] lkoLength = new int[] { -1 };
		int[] lkoOffset = new int[] { -1 };
		byte[][] lkoBuffer = new byte[][] { null };
		doAnswer((stubInvo) -> {
			byte[] pBuffer = stubInvo.getArgument(0);
			int pOffset = stubInvo.getArgument(1);
			int pLength = stubInvo.getArgument(2);
			stubInvo.callRealMethod();
			lkoBuffer[0] = pBuffer;
			lkoOffset[0] = pOffset;
			lkoLength[0] = pLength;
			return null;
		}).when(lko).data(any(byte[].class), anyInt(), anyInt());
		final byte[] readBuffer = new byte[23];
		assertEquals(null, lkoBuffer[0]);
		ois.read(readBuffer);
		assertEquals(null, lkoBuffer[0]);
		ois.add(lko);
		for (;;) {
			if (bais.available() >= 2048) {
				final int result = ois.read(readBuffer);
				if (result == -1) {
					ois.close();
					break;
				} else {
					assertEquals(readBuffer, lkoBuffer[0]);
					assertEquals(0, lkoOffset[0]);
					assertEquals(readBuffer.length, lkoLength[0]);
				}
			} else {
				final int res = Math.min(11, bais.available());
				final int result = ois.read(readBuffer, 1, 11);
				if (result == -1) {
					ois.close();
					break;
				} else {
					assertEquals(readBuffer, lkoBuffer[0]);
					assertEquals(1, lkoOffset[0]);
					assertEquals(res, lkoLength[0]);
				}
			}
		}
	}

}

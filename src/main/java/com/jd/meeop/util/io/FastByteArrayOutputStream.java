/*
 * Copyright 2004-2016 JD.com Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.meeop.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区
 * @ClassName: FastByteArrayOutputStream
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class FastByteArrayOutputStream extends OutputStream {

	private final FastByteBuffer buffer;

	public FastByteArrayOutputStream() {
		this(1024);
	}

	public FastByteArrayOutputStream(int size) {
		buffer = new FastByteBuffer(size);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		buffer.append(b, off, len);
	}

	@Override
	public void write(int b) {
		buffer.append((byte) b);
	}

	public int size() {
		return buffer.size();
	}

	/**
	 * 此方法无任何效果，当流被关闭后不会抛出IOException
	 */
	@Override
	public void close() {
		// nop
	}

	public void reset() {
		buffer.reset();
	}

	public void writeTo(OutputStream out) throws IOException {
		int index = buffer.index();
		for (int i = 0; i < index; i++) {
			byte[] buf = buffer.array(i);
			out.write(buf);
		}
		out.write(buffer.array(index), 0, buffer.offset());
	}

	public byte[] toByteArray() {
		return buffer.toArray();
	}

	@Override
	public String toString() {
		return new String(toByteArray());
	}

	public String toString(String enc) throws UnsupportedEncodingException {
		return new String(toByteArray(), enc);
	}
	
	public String toString(Charset charset) {
		return new String(toByteArray(), charset);
	}

}
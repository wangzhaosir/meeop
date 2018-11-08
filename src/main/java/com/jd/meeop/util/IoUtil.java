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
package com.jd.meeop.util;

import com.jd.meeop.util.io.FastByteArrayOutputStream;
import com.jd.meeop.exception.IORuntimeException;
import com.jd.meeop.util.io.StreamProgress;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * IO工具类
 * @ClassName: IoUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class IoUtil {

	/** 默认缓存大小 */
	public static final int DEFAULT_BUFFER_SIZE = 1024;
	/** 数据流末尾 */
	public static final int EOF = -1;

	/**
	 * 静态类不可实例化
	 */
	private IoUtil(){}

	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IOException
	 */
	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IOException
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IOException
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IOException {
		char[] buffer = new char[bufferSize];
		long size = 0;
		int readSize;
		if (null != streamProgress) {
			streamProgress.start();
		}
		while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
			writer.write(buffer, 0, readSize);
			size += readSize;
			writer.flush();
			if (null != streamProgress) {
				streamProgress.progress(size);
			}
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}

	/**
	 * 拷贝流，使用默认Buffer大小
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IOException
	 */
	public static long copy(InputStream in, OutputStream out) throws IOException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IOException
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IOException
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IOException {
		if (null == in) {
			throw new NullPointerException("InputStream is null!");
		}
		if (null == out) {
			throw new NullPointerException("OutputStream is null!");
		}
		if (bufferSize <= 0) {
			bufferSize = DEFAULT_BUFFER_SIZE;
		}

		byte[] buffer = new byte[bufferSize];
		long size = 0;
		if (null != streamProgress) {
			streamProgress.start();
		}
		for (int readSize = -1; (readSize = in.read(buffer)) != EOF;) {
			out.write(buffer, 0, readSize);
			size += readSize;
			out.flush();
			if (null != streamProgress) {
				streamProgress.progress(size);
			}
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}

	/**
	 * 拷贝文件流，使用NIO
	 * 
	 * @param in 输入
	 * @param out 输出
	 * @return 拷贝的字节数
	 * @throws IOException
	 */
	public static long copy(FileInputStream in, FileOutputStream out) throws IOException {
		if (null == in) {
			throw new NullPointerException("FileInputStream is null!");
		}
		if (null == out) {
			throw new NullPointerException("FileOutputStream is null!");
		}

		FileChannel inChannel = in.getChannel();
		FileChannel outChannel = out.getChannel();

		return inChannel.transferTo(0, inChannel.size(), outChannel);
	}
	// -------------------------------------------------------------------------------------- Copy end

	/**
	 * 获得一个文件读取器
	 * 
	 * @param in 输入流
	 * @param charsetName 字符集名称
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(InputStream in, String charsetName) throws IOException {
		return getReader(in, Charset.forName(charsetName));
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) throws IOException {
		if (null == in) {
			return null;
		}

		InputStreamReader reader = null;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}

		return new BufferedReader(reader);
	}

	/**
	 * 从流中读取bytes
	 * 
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream in) throws IOException {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		copy(in, out);
		return out.toByteArray();
	}
	
	/**
	 * 读取指定长度的byte数组
	 * @param in {@link InputStream}
	 * @param length 长度
	 * @return bytes
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream in, int length) throws IOException {
		byte[] b = new byte[length];
		in.read(b);
		return b;
	}
	
	/**
	 * 读取进制字符串
	 * @param in {@link InputStream}
	 * @param length 长度
	 * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
	 * @return 16进制字符串
	 * @throws IOException
	 */
	public static String readHex(InputStream in, int length, boolean toLowerCase) throws IOException{
		return HexUtil.encodeHexStr(readBytes(in, length), toLowerCase);
	}
	
	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用大写
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IOException
	 */
	public static String readHex28Upper(InputStream in) throws IOException{
		return readHex(in, 28, false);
	}
	
	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用小写
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IOException
	 */
	public static String readHex28Lower(InputStream in) throws IOException{
		return readHex(in, 28, false);
	}

	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String read(InputStream in, String charsetName) throws IOException {
		FastByteArrayOutputStream out = read(in);
		return StringUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
	}

	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String read(InputStream in, Charset charset) throws IOException {
		FastByteArrayOutputStream out = read(in);
		return null == charset ? out.toString() : out.toString(charset);
	}

	/**
	 * 从流中读取内容，读到输出流中
	 * 
	 * @param in 输入流
	 * @return 输出流
	 * @throws IOException
	 */
	public static FastByteArrayOutputStream read(InputStream in) throws IOException {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		copy(in, out);
		return out;
	}

	/**
	 * 从流中读取内容，读到输出流中
	 * 
	 * @param in 输入流
	 * @return 输出流
	 * @throws IOException
	 */
	public static <T> T readObj(InputStream in) throws IOException {
		if (in == null) {
			throw new IllegalArgumentException("The InputStream must not be null");
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(in);
			@SuppressWarnings("unchecked") // may fail with CCE if serialised form is incorrect
			final T obj = (T) ois.readObject();
			return obj;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 从Reader中读取String
	 * 
	 * @param reader Reader
	 * @return String
	 * @throws IOException
	 */
	public static String read(Reader reader) throws IOException {
		final StringBuilder builder = StringUtil.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		while (-1 != reader.read(buffer)) {
			builder.append(buffer.flip().toString());
		}
		return builder.toString();
	}
	
	/**
	 * 从FileChannel中读取内容
	 * 
	 * @param fileChannel 文件管道
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String read(FileChannel fileChannel, String charsetName) throws IOException {
		return read(fileChannel, CharsetUtil.charset(charsetName));
	}

	/**
	 * 从FileChannel中读取内容
	 * 
	 * @param fileChannel 文件管道
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String read(FileChannel fileChannel, Charset charset) throws IOException {
		final MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
		return StringUtil.str(buffer, charset);
	}
	
	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charsetName 字符集
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IOException
	 */
	public static <T extends Collection<String>> T readLines(InputStream in, String charsetName, T collection) throws IOException {
		return readLines(in, CharsetUtil.charset(charsetName), collection);
	}

	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IOException
	 */
	public static <T extends Collection<String>> T readLines(InputStream in, Charset charset, T collection) throws IOException {
		// 从返回的内容中读取所需内容
		BufferedReader reader =getReader(in, charset);
		String line = null;
		while ((line = reader.readLine()) != null) {
			collection.add(line);
		}

		return collection;
	}
	
	/**
	 * String 转为流
	 * 
	 * @param content 内容
	 * @param charsetName 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, String charsetName) {
		return toStream(content, CharsetUtil.charset(charsetName));
	}

	/**
	 * String 转为流
	 * 
	 * @param content 内容
	 * @param charset 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, Charset charset) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(StringUtil.bytes(content, charset));
	}
	
	/**
	 * 文件转为流
	 * @param file 文件
	 * @return {@link FileInputStream}
	 */
	public static FileInputStream toStream(File file){
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 * 
	 * @param out 输出流
	 * @param charset 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IOException
	 */
	public static void write(OutputStream out, String charset, boolean isCloseOut, Object... contents) throws IOException {
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(out, charset);
			for (Object content : contents) {
				if (content != null) {
					osw.write(ConvertUtil.toStr(content, StringUtil.EMPTY));
					osw.flush();
				}
			}
		} catch (Exception e) {
			throw new IOException("Write content to OutputStream error!", e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 将多部分内容写到流中
	 * 
	 * @param out 输出流
	 * @param charset 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容
	 * @throws IOException
	 */
	public static void writeObjects(OutputStream out, String charset, boolean isCloseOut, Serializable... contents) throws IOException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
					osw.flush();
				}
			}
		} catch (Exception e) {
			throw new IOException("Write content to OutputStream error!", e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 关闭
	 * 
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (closeable == null) return;
		try {
			closeable.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 关闭
	 * 
	 * @param closeable 被关闭的对象
	 * @since 1.7
	 */
	public static void close(AutoCloseable closeable) {
		if (closeable == null) return;
		try {
			closeable.close();
		} catch (Exception e) {
		}
	}
}

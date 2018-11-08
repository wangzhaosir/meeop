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
package com.jd.meeop.util.http;

import com.jd.meeop.util.ConvertUtil;
import com.jd.meeop.util.HttpUtil;
import com.jd.meeop.util.StringUtil;
import com.jd.meeop.exception.HttpException;
import com.jd.meeop.util.IoUtil;
import com.jd.meeop.util.io.FastByteArrayOutputStream;

import java.io.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

/**
 * Http响应类
 * @ClassName: HttpResponse
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class HttpResponse extends HttpBase<HttpResponse> {
	
	/** 读取服务器返回的流保存至内存 */
	private FastByteArrayOutputStream out;

	/**
	 * 读取响应信息
	 * 
	 * @param httpConnection Http连接对象
	 * @return HttpResponse
	 */
	public static HttpResponse readResponse(HttpConnection httpConnection) {
		final HttpResponse httpResponse = new HttpResponse();
		
		try {
			httpResponse.status = httpConnection.responseCode();
			httpResponse.headers =  httpConnection.headers();
			httpResponse.charset = httpConnection.charset();
			
			InputStream in;
			if(httpResponse.status < HttpStatus.HTTP_BAD_REQUEST){
				in = httpConnection.getInputStream();
			}else{
				in = httpConnection.getErrorStream();
			}
			httpResponse.readBody(in);
		} catch (IOException e) {
			if(e instanceof FileNotFoundException){
				//服务器无返回内容，忽略之
			}else{
				throw new HttpException(e.getMessage(), e);
			}
		}
		
		return httpResponse;
	}

	/** 响应状态码 */
	private int status;

	public HttpResponse() {
	}

	/**
	 * 获取状态码
	 * 
	 * @return 状态码
	 */
	public int getStatus() {
		return status;
	}
	
	// ---------------------------------------------------------------- Http Response Header start
	/**
	 * 获取内容编码
	 * @return String
	 */
	public String contentEncoding() {
		return header(Header.CONTENT_ENCODING);
	}
	
	/**
	 * @return 是否为gzip压缩过的内容
	 */
	public boolean isGzip(){
		String contentEncoding = contentEncoding();
		return contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip");
	}
	// ---------------------------------------------------------------- Http Response Header end
	
	// ---------------------------------------------------------------- Body start
	/**
	 * 获得服务区响应流
	 * @return 响应流
	 */
	public InputStream bodyStream(){
		return new ByteArrayInputStream(this.out.toByteArray());
	}
	
	/**
	 * 获取响应流字节码
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		if(null == this.out){
			return null;
		}
		return this.out.toByteArray();
	}

	/**
	 * 获取响应主体
	 * @return String
	 */
	public String body() {
		try {
			return HttpUtil.getString(bodyStream(), this.charset, null == this.charset);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	// ---------------------------------------------------------------- Body end
	
	@Override
	public String toString() {
		StringBuilder sb = StringUtil.builder();
		sb.append("Request Headers: ").append(StringUtil.CRLF);
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ").append(entry).append(StringUtil.CRLF);
		}
		
		sb.append("Request Body: ").append(StringUtil.CRLF);
		sb.append("    ").append(this.body()).append(StringUtil.CRLF);
		
		return sb.toString();
	}
	
	// ---------------------------------------------------------------- Private method start
	/**
	 * 读取主体
	 * @param in 输入流
	 * @return 自身
	 * @throws IOException
	 */
	private void readBody(InputStream in) throws IOException{
		if(isGzip()){
			in = new GZIPInputStream(in);
		}
		
		int contentLength  = ConvertUtil.toInt(header(Header.CONTENT_LENGTH), 0);
		this.out = contentLength > 0 ? new FastByteArrayOutputStream(contentLength) : new FastByteArrayOutputStream();
		try {
			IoUtil.copy(in, this.out);
		} catch (EOFException e) {
			//忽略读取HTTP流中的EOF错误
		}
	}
	// ---------------------------------------------------------------- Private method end
}

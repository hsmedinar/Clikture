package com.abyte.piclt2.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.james.mime4j.message.AbstractBody;
import org.apache.james.mime4j.message.BinaryBody;

public class ByteBody extends AbstractBody implements BinaryBody, ContentBody {

	private final byte[] mContent;

	public ByteBody(final byte[] content) {
		super();
		if (content == null) {
			throw new IllegalArgumentException("File may not be null");
		}
		this.mContent = content;
	}

	public InputStream getInputStream() throws IOException {
		return (new ByteArrayInputStream(mContent));
	}

	public void writeTo(final OutputStream out, int mode) throws IOException {
		if (out == null) {
			throw new IllegalArgumentException("Output stream may not be null");
		}
		out.write(mContent, 0, mContent.length);
	}

	public String getTransferEncoding() {
		return "quoted-printable";
	}

	public String getCharset() {
		return null;
	}

	public String getMimeType() {
		return null;
	}

	public Map<?, ?> getContentTypeParameters() {
		return Collections.EMPTY_MAP;
	}

	public String getMediaType() {
		return null;
	}

	public String getSubType() {
		return null;
	}

	public long getContentLength() {
		return this.mContent.length;
	}

	public String getFilename() {
		return null;
	}
	
	
}
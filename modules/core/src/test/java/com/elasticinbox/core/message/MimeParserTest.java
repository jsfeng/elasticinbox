/**
 * Copyright (c) 2011-2013 Optimax Software Ltd.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Optimax Software, ElasticInbox, nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.elasticinbox.core.message;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import com.elasticinbox.core.model.Message;
import com.elasticinbox.core.model.MimePart;

public class MimeParserTest
{
	private final static String TEST_INLINE_ATTACH_FILE = "../../itests/src/test/resources/03-inline-attach.eml";

	/**
	 * Loop through message parts and check if retrieval by PartId is consistent
	 * with retrieval by ContentId.
	 * 
	 * @throws IOException
	 * @throws MimeParserException
	 */
	@Test
	public void testGetInputStreamByPartIdAndContentId() throws IOException, MimeParserException
	{
		File file = new File(TEST_INLINE_ATTACH_FILE);
		InputStream in = new FileInputStream(file);

		MimeParser mp = new MimeParser(in);
		Message message = mp.getMessage();
		
		Map<String, MimePart> parts = message.getParts();
		
		for (String partId : parts.keySet())
		{
			MimePart part = parts.get(partId);
			InputStream attachmentContentByPartId = mp.getInputStreamByPartId(partId);
			String attachmentHashByPartId = DigestUtils.md5Hex(attachmentContentByPartId);
			
			InputStream attachmentContentByContentId = mp.getInputStreamByContentId(part.getContentId());
			String attachmentHashByContentId = DigestUtils.md5Hex(attachmentContentByContentId);
			
			assertEquals(attachmentHashByPartId, attachmentHashByContentId);
		}
	}

}

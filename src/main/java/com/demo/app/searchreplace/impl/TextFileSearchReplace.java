package com.demo.app.searchreplace.impl;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import com.demo.app.searchreplace.api.Operation;

@Service
public class TextFileSearchReplace implements Operation {

	@Override
	public String performOperation(String searchWord, InputStream fileInputStream) throws IOException {
		
		//String defaultBaseDir = System.getProperty("java.io.tmpdir");
		
		String defaultBaseDir=System.getProperty("user.dir")+"/";
		System.out.println(defaultBaseDir);
		byte[] buffer = new byte[fileInputStream.available()];
		fileInputStream.read(buffer);

		File targetFile = new File(defaultBaseDir + "/targetFile.tmp");
		OutputStream outStream = new FileOutputStream(targetFile);
		outStream.write(buffer);

		Path path = Paths.get(defaultBaseDir + "/targetFile.tmp");
		Charset charset = StandardCharsets.ISO_8859_1;
		String content = new String(Files.readAllBytes(path), charset);

		if (content.contains("Google") || content.contains("Amazon") || content.contains("Deloitte")
				|| content.contains("Microsoft") || content.contains("Oracle")) {

			content = content.replaceAll(searchWord, searchWord +"©");
		}
		final String resultFileName = UUID.randomUUID().toString() + ".txt";
		Files.write(Paths.get(defaultBaseDir +"/" +resultFileName),
				content.getBytes(charset));

		IOUtils.closeQuietly(fileInputStream);
		IOUtils.closeQuietly(outStream);

		return resultFileName;

	}

}

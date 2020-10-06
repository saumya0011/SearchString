package com.demo.app.searchreplace.api;

import java.io.IOException;
import java.io.InputStream;

public interface Operation {
	public String performOperation(String searchWord, InputStream fileInputStream) throws IOException;
}

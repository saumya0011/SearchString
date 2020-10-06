package com.demo.app.searchreplace.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.app.searchreplace.api.Operation;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SearchReplaceController {
	@Autowired
	private Operation operation;

	@GetMapping("/search")
	public String sayHello(Model theModel) {
		return "SearchReplace";
	}

	@PostMapping("/upload")
	public String searchAndReplace(@RequestParam("uploadFile") MultipartFile file,
			@RequestParam("fileType") String fileType, @RequestParam("searchWord") String searchWord, Model theModel) {
		try {
			if (fileType.equalsIgnoreCase("text") || fileType.equalsIgnoreCase("txt")) {
				theModel.addAttribute("resultFileName", operation.performOperation(searchWord, file.getInputStream()));
			}
		} catch (IOException e) {
			log.error("IO exception occured while reading file or the File is not a text file");

		}
		return "SearchReplace";
	}

	@GetMapping("/download/{fileName}")
	public String donwloadFile(@PathVariable("fileName") String downloadFileName, HttpServletResponse response)
			throws IOException {
		if (!StringUtils.isEmpty(downloadFileName)) {
			File fileToDownload = new File(System.getProperty("user.dir") + "/" + downloadFileName);
			String fileName = fileToDownload.getName();
			response.setContentType("text/html;charset=ISO_8859_1");
			response.setStatus(200);
			String headerValue = String.format("attachment; filename=\"%s\"", fileName);
			response.setHeader("Content-Disposition", headerValue);
			FileInputStream inputStream = new FileInputStream(fileToDownload.getAbsolutePath());
			try {
				int c;
				while ((c = inputStream.read()) != -1) {
					response.getWriter().write(c);
				}
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				response.getWriter().close();
			}
		}
		return "SearchReplace";
	}
}

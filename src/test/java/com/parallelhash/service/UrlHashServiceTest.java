package com.parallelhash.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UrlHashServiceTest {

	@Autowired
	private UrlHashService urlHashService;

	@Test
	public void shouldConvertFileToList() {
		List<String>urls = urlHashService.inputFileToList();
		assertEquals(3, urls.size());
	}

	@Test
	public void shouldCalculateHash() {
		String testString = "test-string";
		InputStream inputStream = new ByteArrayInputStream(testString.getBytes());
		assertEquals("661f8009fa8e56a9d0e94a0a644397d7", urlHashService.calculateHash(inputStream));
	}

}

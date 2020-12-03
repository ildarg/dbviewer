package com.atcproject.dbviewer;

import com.atcproject.dbviewer.model.ConnectionDetail;
import com.atcproject.dbviewer.repository.ConnectionDetailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class DBViewerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ConnectionDetailRepository connectionDetailRepository;

	private ConnectionDetail createTestConnectionDetails(Long id) {
		ConnectionDetail connectionDetail = new ConnectionDetail();
		connectionDetail.setId(id);
		connectionDetail.setName("name");
		connectionDetail.setHostname("localhost");
		connectionDetail.setDbname("dbname");
		connectionDetail.setUsername("user");
		connectionDetail.setPassword("pass");
		return connectionDetail;
	}

	String expectedTestConnectionDetailsJson = "{\"id\":1,\"name\":\"name\",\"hostname\":\"localhost\",\"port\":null,\"dbname\":\"dbname\",\"username\":\"user\",\"password\":\"pass\"}";

	@Test
	void shouldReturnConnections() throws Exception {
		when(connectionDetailRepository.findAll()).thenReturn(List.of(createTestConnectionDetails(1L)));
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/connections")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(String.format("[%s]", expectedTestConnectionDetailsJson)))
				.andDo(document("get connections"));;
	}

	@Test
	void shouldPostConnection() throws Exception {
		ConnectionDetail testConnectionDetails = createTestConnectionDetails(1L);
		when(connectionDetailRepository.save(any())).thenReturn(testConnectionDetails);
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/connections")
						.content(asJsonString(testConnectionDetails))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedTestConnectionDetailsJson))
				.andDo(document("post connection"));;;
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

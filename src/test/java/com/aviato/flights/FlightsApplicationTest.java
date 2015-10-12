/*
 * Copyright 2014 the original author or authors.
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
package com.aviato.flights;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FlightsApplication.class)
@WebAppConfiguration
public class FlightsApplicationTest {

	@Autowired
	private FlightDetailRepository flightDetailRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	}

	@Test
	public void getFlightDetails() throws Exception {

		Search search = new Search("DFW", "SFO", "2015-10-06", "2015-10-08", 1, 0, 0, 0, 0, Cabin.Coach);

		//@formatter:off
		MvcResult mvcResult = mockMvc.perform(
				post("/flightSearch")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(search)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		//@formatter:on

		FlightSearchResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), FlightSearchResponse.class);

		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getDepartures().size());
	}

}

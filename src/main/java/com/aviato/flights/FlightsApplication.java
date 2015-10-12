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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

@SpringBootApplication
@EnableGemfireRepositories
@RestController
public class FlightsApplication implements CommandLineRunner {

	@Autowired
	private FlightDetailRepository flightDetailRepository;

	@Bean
	public ObjectMapper objectMapperFactory() {
		ObjectMapper objectMapper = new ObjectMapper();

		// Serialize LocalDate as String "yyyy-MM-dd" (instead of int array [yyyy,MM,dd])
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return objectMapper;
	}

	@Bean
	CacheFactoryBean cacheFactoryBean(@Qualifier("gemfireProperties") Optional<Properties> gemfireProperties) {

		CacheFactoryBean cacheFactory = new CacheFactoryBean();
		gemfireProperties.ifPresent(props -> cacheFactory.setProperties(props));

		return cacheFactory;
	}

	@Profile("cloud")
	static class Cloud {

		@Bean
		ClientRegionFactoryBean<String, FlightDetail> clientRegionFactory(final ClientCache cache) {
			return new ClientRegionFactoryBean<String, FlightDetail>() {
				{
					setCache(cache);
					setName("FlightDetail");
					setShortcut(ClientRegionShortcut.PROXY);
				}
			};
		}
	}

	@Profile("default")
	static class Default {

		@Bean
		Properties gemfireProperties() {
			Properties properties = new Properties();
			// Run in "local mode"
			properties.setProperty("mcast-port", "0");
			return properties;
		}

		@Bean
		LocalRegionFactoryBean<String, FlightDetail> localRegionFactory(final GemFireCache cache) {
			return new LocalRegionFactoryBean<String, FlightDetail>() {
				{
					setCache(cache);
					setName("aviato");
				}
			};
		}
	}

	@Override
	public void run(String... args) throws Exception {

		LocalDate today = LocalDate.now();

		flightDetailRepository.save(new FlightDetail("United", "DFW", "SFO", today.plusDays(1), today.plusDays(1), BigDecimal.valueOf(349), 227));
		flightDetailRepository.save(new FlightDetail("American", "DFW", "SFO", today.plusDays(1), today.plusDays(1), BigDecimal.valueOf(368), 230));
		flightDetailRepository.save(new FlightDetail("United", "SFO", "DFW", today.plusDays(3), today.plusDays(3), BigDecimal.valueOf(357), 217));
		flightDetailRepository.save(new FlightDetail("American", "SFO", "DFW", today.plusDays(3), today.plusDays(3), BigDecimal.valueOf(352), 220));

		flightDetailRepository.findAll().forEach(System.out::println);
	}

	@RequestMapping(value = "/flightSearch", method = RequestMethod.POST)
	public FlightSearchResponse flightSearch(@RequestBody Search search) {

		FlightSearchResponse response = new FlightSearchResponse();

		List<FlightDetail> departures = flightDetailRepository.findByOriginAndDestinationAndDepartureTime(search.getOrigin(), search.getDestination(), LocalDate.parse(search.getDepartDate()));
		response.setDepartures(departures);

		List<FlightDetail> returns = flightDetailRepository.findByOriginAndDestinationAndDepartureTime(search.getDestination(), search.getOrigin(), LocalDate.parse(search.getReturnDate()));
		response.setReturns(returns);

		return response;
	}

	@RequestMapping("/flightSearch")
	public String whoami() {
		return "{ \"id\": \"1234\", \"content\": \"Wasabi!\" }";
	}

	public static void main(String[] args) {
		SpringApplication.run(FlightsApplication.class, args);
	}

}

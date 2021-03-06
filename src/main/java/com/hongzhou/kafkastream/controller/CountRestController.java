package com.hongzhou.kafkastream.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hongzhou.kafkastream.channel.AnalyticsBinding;

@RestController
public class CountRestController {

	private final QueryableStoreRegistry registry;

	public CountRestController(QueryableStoreRegistry registry) {
		super();
		this.registry = registry;
	}

	@GetMapping("/counts")
	public Map<String, Long> counts() {
		Map<String, Long> counts = new HashMap<>();
		
		ReadOnlyKeyValueStore<String, Long> queryableStoreType = this.registry
				.getQueryableStoreType(AnalyticsBinding.PAGE_COUNT_MV, QueryableStoreTypes.keyValueStore());
		
		KeyValueIterator<String, Long> all = queryableStoreType.all();
		
		while (all.hasNext()) {
			KeyValue<String, Long> value = all.next();
			counts.put(value.key, value.value);
		}

		return counts;
	}

}

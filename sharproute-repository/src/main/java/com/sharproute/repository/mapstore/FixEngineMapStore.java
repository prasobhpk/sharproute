package com.sharproute.repository.mapstore;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hazelcast.core.MapStore;
import com.sharproute.common.object.FixEngine;
import com.sharproute.repository.FixEngineRepository;

public class FixEngineMapStore implements MapStore<Integer, FixEngine> {
	
	@Resource
	private FixEngineRepository fixEngineRepository;

	@Override
	public FixEngine load(Integer key) {
		return fixEngineRepository.findOne(key);
	}

	@Override
	public Map<Integer, FixEngine> loadAll(Collection<Integer> keys) {
		Iterable<FixEngine> fixEngines = fixEngineRepository.findAll(keys);
		Map<Integer, FixEngine> map = new HashMap<>();
		for (FixEngine fixEngine : fixEngines) {
			map.put(fixEngine.getUid(), fixEngine);
		}
		return map;
	}

	@Override
	public Set<Integer> loadAllKeys() {
		Iterable<FixEngine> fixEngines = fixEngineRepository.findAll();
		Set<Integer> set = new HashSet<>();
		for (FixEngine fixEngine : fixEngines) {
			set.add(fixEngine.getUid());
		}
		return set;
	}

	@Override
	public void store(Integer key, FixEngine value) {
		fixEngineRepository.save(value);
	}

	@Override
	public void storeAll(Map<Integer, FixEngine> map) {
		fixEngineRepository.save(map.values());
	}

	@Override
	public void delete(Integer key) {
		fixEngineRepository.delete(key);
	}

	@Override
	public void deleteAll(Collection<Integer> keys) {
		for (Integer integer : keys) {
			fixEngineRepository.delete(integer);
		}
	}

}

package com.sharproute.repository.mapstore;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.hazelcast.core.MapStore;
import com.sharproute.common.object.FixServer;
import com.sharproute.repository.FixServerRepository;

@Named
public class FixServerMapStore implements MapStore<Integer, FixServer> {
	
	@Inject
	private FixServerRepository fixServerRepository;

	@Override
	public FixServer load(Integer key) {
		return fixServerRepository.findOne(key);
	}

	@Override
	public Map<Integer, FixServer> loadAll(Collection<Integer> keys) {
		Iterable<FixServer> fixEngines = fixServerRepository.findAll(keys);
		Map<Integer, FixServer> map = new HashMap<>();
		for (FixServer fixEngine : fixEngines) {
			map.put(fixEngine.getUid(), fixEngine);
		}
		return map;
	}

	@Override
	public Set<Integer> loadAllKeys() {
		Iterable<FixServer> fixEngines = fixServerRepository.findAll();
		Set<Integer> set = new HashSet<>();
		for (FixServer fixEngine : fixEngines) {
			set.add(fixEngine.getUid());
		}
		return set;
	}

	@Override
	public void store(Integer key, FixServer value) {
		fixServerRepository.save(value);
	}

	@Override
	public void storeAll(Map<Integer, FixServer> map) {
		fixServerRepository.save(map.values());
	}

	@Override
	public void delete(Integer key) {
		fixServerRepository.delete(key);
	}

	@Override
	public void deleteAll(Collection<Integer> keys) {
		for (Integer integer : keys) {
			fixServerRepository.delete(integer);
		}
	}

}

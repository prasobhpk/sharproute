package com.sharproute.repository.mapstore;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hazelcast.core.MapStore;
import com.sharproute.common.object.FixSession;
import com.sharproute.repository.FixSessionRepository;

@Component
public class FixSessionMapStore implements MapStore<Integer, FixSession> {
	
	@Resource
	private FixSessionRepository fixSessionRepository;

	@Override
	public FixSession load(Integer key) {
		return fixSessionRepository.findOne(key);
	}

	@Override
	public Map<Integer, FixSession> loadAll(Collection<Integer> keys) {
		Iterable<FixSession> fixSessions = fixSessionRepository.findAll(keys);
		Map<Integer, FixSession> map = new HashMap<>();
		for (FixSession fixSession : fixSessions) {
			map.put(fixSession.getUid(), fixSession);
		}
		return map;
	}

	@Override
	public Set<Integer> loadAllKeys() {
		Iterable<FixSession> fixSessions = fixSessionRepository.findAll();
		Set<Integer> set = new HashSet<>();
		for (FixSession fixSession : fixSessions) {
			set.add(fixSession.getUid());
		}
		return set;
	}

	@Override
	public void store(Integer key, FixSession value) {
		fixSessionRepository.save(value);
	}

	@Override
	public void storeAll(Map<Integer, FixSession> map) {
		fixSessionRepository.save(map.values());
	}

	@Override
	public void delete(Integer key) {
		fixSessionRepository.delete(key);
	}

	@Override
	public void deleteAll(Collection<Integer> keys) {
		for (Integer integer : keys) {
			fixSessionRepository.delete(integer);
		}
	}

}

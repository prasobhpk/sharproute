package com.sharproute.repository;

import org.springframework.data.repository.CrudRepository;

import com.sharproute.common.object.FixEngine;
import com.sharproute.common.object.FixSession;

public interface FixEngineRepository extends CrudRepository<FixEngine, Integer> {

}

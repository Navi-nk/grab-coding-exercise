package com.navi.grabcodingexercise.repository;

import com.navi.grabcodingexercise.entity.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Long> {

    Optional<Group> findByGroupId(String groupId);
}

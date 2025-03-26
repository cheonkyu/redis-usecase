package io.cheonkyu.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.cheonkyu.data.Person;

public interface PersonRepository extends CrudRepository<Person, String> {
  public Optional<Person> findById(String id);
}

package io.cheonkyu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.cheonkyu.data.Person;
import io.cheonkyu.repository.PersonRepository;

@Service
public class PersonService {
  @Autowired
  private PersonRepository repo;

  public long saveAndCount() {

    Person rand = new Person();
    rand.setFirstname("test");
    rand.setLastname("test11");

    Person p2 = new Person();
    p2.setFirstname("user2");
    p2.setLastname("user2");

    repo.save(rand);
    repo.save(p2);

    repo.findById(rand.getId());

    return repo.count();
  }
}

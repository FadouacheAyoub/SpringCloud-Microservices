package me.fadouache.orderservice.repositories;

import me.fadouache.orderservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface OrderRepository extends JpaRepository<Order, String> {
    Collection<Order> findAllByCustomerId(Long id);
}

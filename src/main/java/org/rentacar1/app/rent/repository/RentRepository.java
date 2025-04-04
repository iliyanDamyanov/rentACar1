package org.rentacar1.app.rent.repository;

import org.rentacar1.app.rent.model.Rent;
import org.rentacar1.app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RentRepository extends JpaRepository<Rent, UUID> {
    List<Rent> id(UUID id);

    List<Rent> findByUser(User user);

}

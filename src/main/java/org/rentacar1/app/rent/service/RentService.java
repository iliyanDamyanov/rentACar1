package org.rentacar1.app.rent.service;

import org.rentacar1.app.rent.model.Rent;
import org.rentacar1.app.rent.repository.RentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RentService {
    public final RentRepository rentRepository;

    public RentService(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    public List<Rent> getAllRents(){
       return rentRepository.findAll();
    }

    public Rent getRentById(UUID id){
        return rentRepository.findById(id).orElseThrow(() -> new RuntimeException("Rent not found"));
    }

    public Rent saveRent(Rent rent){
        return rentRepository.save(rent);
    }

    public void deleteRent(UUID id){
        rentRepository.deleteById(id);
    }

}

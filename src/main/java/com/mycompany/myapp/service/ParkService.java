package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ParkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Park}.
 */
public interface ParkService {
    /**
     * Save a park.
     *
     * @param parkDTO the entity to save.
     * @return the persisted entity.
     */
    ParkDTO save(ParkDTO parkDTO);

    /**
     * Partially updates a park.
     *
     * @param parkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ParkDTO> partialUpdate(ParkDTO parkDTO);

    /**
     * Get all the parks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" park.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParkDTO> findOne(Long id);

    /**
     * Delete the "id" park.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

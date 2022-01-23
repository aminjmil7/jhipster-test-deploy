package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EventsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Events}.
 */
public interface EventsService {
    /**
     * Save a events.
     *
     * @param eventsDTO the entity to save.
     * @return the persisted entity.
     */
    EventsDTO save(EventsDTO eventsDTO);

    /**
     * Partially updates a events.
     *
     * @param eventsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventsDTO> partialUpdate(EventsDTO eventsDTO);

    /**
     * Get all the events.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" events.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventsDTO> findOne(Long id);

    /**
     * Delete the "id" events.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

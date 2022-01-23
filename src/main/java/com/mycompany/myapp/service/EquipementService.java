package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EquipementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Equipement}.
 */
public interface EquipementService {
    /**
     * Save a equipement.
     *
     * @param equipementDTO the entity to save.
     * @return the persisted entity.
     */
    EquipementDTO save(EquipementDTO equipementDTO);

    /**
     * Partially updates a equipement.
     *
     * @param equipementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EquipementDTO> partialUpdate(EquipementDTO equipementDTO);

    /**
     * Get all the equipements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EquipementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" equipement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EquipementDTO> findOne(Long id);

    /**
     * Delete the "id" equipement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

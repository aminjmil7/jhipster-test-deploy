package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MediaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Media}.
 */
public interface MediaService {
    /**
     * Save a media.
     *
     * @param mediaDTO the entity to save.
     * @return the persisted entity.
     */
    MediaDTO save(MediaDTO mediaDTO);

    /**
     * Partially updates a media.
     *
     * @param mediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MediaDTO> partialUpdate(MediaDTO mediaDTO);

    /**
     * Get all the media.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MediaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" media.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MediaDTO> findOne(Long id);

    /**
     * Delete the "id" media.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

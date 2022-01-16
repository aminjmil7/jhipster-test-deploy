package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Park;
import com.mycompany.myapp.repository.ParkRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Park}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ParkResource {

    private final Logger log = LoggerFactory.getLogger(ParkResource.class);

    private static final String ENTITY_NAME = "park";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParkRepository parkRepository;

    public ParkResource(ParkRepository parkRepository) {
        this.parkRepository = parkRepository;
    }

    /**
     * {@code POST  /parks} : Create a new park.
     *
     * @param park the park to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new park, or with status {@code 400 (Bad Request)} if the park has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parks")
    public ResponseEntity<Park> createPark(@RequestBody Park park) throws URISyntaxException {
        log.debug("REST request to save Park : {}", park);
        if (park.getId() != null) {
            throw new BadRequestAlertException("A new park cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Park result = parkRepository.save(park);
        return ResponseEntity
            .created(new URI("/api/parks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parks/:id} : Updates an existing park.
     *
     * @param id the id of the park to save.
     * @param park the park to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated park,
     * or with status {@code 400 (Bad Request)} if the park is not valid,
     * or with status {@code 500 (Internal Server Error)} if the park couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parks/{id}")
    public ResponseEntity<Park> updatePark(@PathVariable(value = "id", required = false) final Long id, @RequestBody Park park)
        throws URISyntaxException {
        log.debug("REST request to update Park : {}, {}", id, park);
        if (park.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, park.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Park result = parkRepository.save(park);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, park.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parks/:id} : Partial updates given fields of an existing park, field will ignore if it is null
     *
     * @param id the id of the park to save.
     * @param park the park to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated park,
     * or with status {@code 400 (Bad Request)} if the park is not valid,
     * or with status {@code 404 (Not Found)} if the park is not found,
     * or with status {@code 500 (Internal Server Error)} if the park couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Park> partialUpdatePark(@PathVariable(value = "id", required = false) final Long id, @RequestBody Park park)
        throws URISyntaxException {
        log.debug("REST request to partial update Park partially : {}, {}", id, park);
        if (park.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, park.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Park> result = parkRepository
            .findById(park.getId())
            .map(
                existingPark -> {
                    if (park.getParkName() != null) {
                        existingPark.setParkName(park.getParkName());
                    }
                    if (park.getParkAddress() != null) {
                        existingPark.setParkAddress(park.getParkAddress());
                    }
                    if (park.getLongtitude() != null) {
                        existingPark.setLongtitude(park.getLongtitude());
                    }
                    if (park.getLatitude() != null) {
                        existingPark.setLatitude(park.getLatitude());
                    }
                    if (park.getVerified() != null) {
                        existingPark.setVerified(park.getVerified());
                    }

                    return existingPark;
                }
            )
            .map(parkRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, park.getId().toString())
        );
    }

    /**
     * {@code GET  /parks} : get all the parks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parks in body.
     */
    @GetMapping("/parks")
    public List<Park> getAllParks() {
        log.debug("REST request to get all Parks");
        return parkRepository.findAll();
    }

    /**
     * {@code GET  /parks/:id} : get the "id" park.
     *
     * @param id the id of the park to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the park, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parks/{id}")
    public ResponseEntity<Park> getPark(@PathVariable Long id) {
        log.debug("REST request to get Park : {}", id);
        Optional<Park> park = parkRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(park);
    }

    /**
     * {@code DELETE  /parks/:id} : delete the "id" park.
     *
     * @param id the id of the park to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parks/{id}")
    public ResponseEntity<Void> deletePark(@PathVariable Long id) {
        log.debug("REST request to delete Park : {}", id);
        parkRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

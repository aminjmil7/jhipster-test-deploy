package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Park;
import com.mycompany.myapp.repository.ParkRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParkResourceIT {

    private static final String DEFAULT_PARK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARK_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PARK_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_LONGTITUDE = 1L;
    private static final Long UPDATED_LONGTITUDE = 2L;

    private static final Long DEFAULT_LATITUDE = 1L;
    private static final Long UPDATED_LATITUDE = 2L;

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final String ENTITY_API_URL = "/api/parks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParkMockMvc;

    private Park park;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Park createEntity(EntityManager em) {
        Park park = new Park()
            .parkName(DEFAULT_PARK_NAME)
            .parkAddress(DEFAULT_PARK_ADDRESS)
            .longtitude(DEFAULT_LONGTITUDE)
            .latitude(DEFAULT_LATITUDE)
            .verified(DEFAULT_VERIFIED);
        return park;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Park createUpdatedEntity(EntityManager em) {
        Park park = new Park()
            .parkName(UPDATED_PARK_NAME)
            .parkAddress(UPDATED_PARK_ADDRESS)
            .longtitude(UPDATED_LONGTITUDE)
            .latitude(UPDATED_LATITUDE)
            .verified(UPDATED_VERIFIED);
        return park;
    }

    @BeforeEach
    public void initTest() {
        park = createEntity(em);
    }

    @Test
    @Transactional
    void createPark() throws Exception {
        int databaseSizeBeforeCreate = parkRepository.findAll().size();
        // Create the Park
        restParkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(park)))
            .andExpect(status().isCreated());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeCreate + 1);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(DEFAULT_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(DEFAULT_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualTo(DEFAULT_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(DEFAULT_VERIFIED);
    }

    @Test
    @Transactional
    void createParkWithExistingId() throws Exception {
        // Create the Park with an existing ID
        park.setId(1L);

        int databaseSizeBeforeCreate = parkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(park)))
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParks() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList
        restParkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(park.getId().intValue())))
            .andExpect(jsonPath("$.[*].parkName").value(hasItem(DEFAULT_PARK_NAME)))
            .andExpect(jsonPath("$.[*].parkAddress").value(hasItem(DEFAULT_PARK_ADDRESS)))
            .andExpect(jsonPath("$.[*].longtitude").value(hasItem(DEFAULT_LONGTITUDE.intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())));
    }

    @Test
    @Transactional
    void getPark() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get the park
        restParkMockMvc
            .perform(get(ENTITY_API_URL_ID, park.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(park.getId().intValue()))
            .andExpect(jsonPath("$.parkName").value(DEFAULT_PARK_NAME))
            .andExpect(jsonPath("$.parkAddress").value(DEFAULT_PARK_ADDRESS))
            .andExpect(jsonPath("$.longtitude").value(DEFAULT_LONGTITUDE.intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPark() throws Exception {
        // Get the park
        restParkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPark() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeUpdate = parkRepository.findAll().size();

        // Update the park
        Park updatedPark = parkRepository.findById(park.getId()).get();
        // Disconnect from session so that the updates on updatedPark are not directly saved in db
        em.detach(updatedPark);
        updatedPark
            .parkName(UPDATED_PARK_NAME)
            .parkAddress(UPDATED_PARK_ADDRESS)
            .longtitude(UPDATED_LONGTITUDE)
            .latitude(UPDATED_LATITUDE)
            .verified(UPDATED_VERIFIED);

        restParkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPark.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPark))
            )
            .andExpect(status().isOk());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(UPDATED_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(UPDATED_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualTo(UPDATED_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void putNonExistingPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, park.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(park))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(park))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(park)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParkWithPatch() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeUpdate = parkRepository.findAll().size();

        // Update the park using partial update
        Park partialUpdatedPark = new Park();
        partialUpdatedPark.setId(park.getId());

        partialUpdatedPark.parkName(UPDATED_PARK_NAME).parkAddress(UPDATED_PARK_ADDRESS);

        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPark))
            )
            .andExpect(status().isOk());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(UPDATED_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(UPDATED_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualTo(DEFAULT_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(DEFAULT_VERIFIED);
    }

    @Test
    @Transactional
    void fullUpdateParkWithPatch() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeUpdate = parkRepository.findAll().size();

        // Update the park using partial update
        Park partialUpdatedPark = new Park();
        partialUpdatedPark.setId(park.getId());

        partialUpdatedPark
            .parkName(UPDATED_PARK_NAME)
            .parkAddress(UPDATED_PARK_ADDRESS)
            .longtitude(UPDATED_LONGTITUDE)
            .latitude(UPDATED_LATITUDE)
            .verified(UPDATED_VERIFIED);

        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPark))
            )
            .andExpect(status().isOk());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(UPDATED_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(UPDATED_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualTo(UPDATED_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, park.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(park))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(park))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(park)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePark() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeDelete = parkRepository.findAll().size();

        // Delete the park
        restParkMockMvc
            .perform(delete(ENTITY_API_URL_ID, park.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

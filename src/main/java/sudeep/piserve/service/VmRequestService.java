package sudeep.piserve.service;

import sudeep.piserve.domain.VmRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link VmRequest}.
 */
public interface VmRequestService {

    /**
     * Save a vmRequest.
     *
     * @param vmRequest the entity to save.
     * @return the persisted entity.
     */
    VmRequest save(VmRequest vmRequest);

    /**
     * Get all the vmRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VmRequest> findAll(Pageable pageable);


    /**
     * Get the "id" vmRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VmRequest> findOne(Long id);

    /**
     * Delete the "id" vmRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

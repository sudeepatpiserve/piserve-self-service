package sudeep.piserve.service;

import sudeep.piserve.domain.Vm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Vm}.
 */
public interface VmService {

    /**
     * Save a vm.
     *
     * @param vm the entity to save.
     * @return the persisted entity.
     */
    Vm save(Vm vm);

    /**
     * Get all the vms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Vm> findAll(Pageable pageable);


    /**
     * Get the "id" vm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vm> findOne(Long id);

    /**
     * Delete the "id" vm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

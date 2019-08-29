package sudeep.piserve.service.impl;

import sudeep.piserve.service.VmService;
import sudeep.piserve.domain.Vm;
import sudeep.piserve.repository.VmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Vm}.
 */
@Service
@Transactional
public class VmServiceImpl implements VmService {

    private final Logger log = LoggerFactory.getLogger(VmServiceImpl.class);

    private final VmRepository vmRepository;

    public VmServiceImpl(VmRepository vmRepository) {
        this.vmRepository = vmRepository;
    }

    /**
     * Save a vm.
     *
     * @param vm the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Vm save(Vm vm) {
        log.debug("Request to save Vm : {}", vm);
        return vmRepository.save(vm);
    }

    /**
     * Get all the vms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Vm> findAll(Pageable pageable) {
        log.debug("Request to get all Vms");
        return vmRepository.findAll(pageable);
    }


    /**
     * Get one vm by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Vm> findOne(Long id) {
        log.debug("Request to get Vm : {}", id);
        return vmRepository.findById(id);
    }

    /**
     * Delete the vm by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vm : {}", id);
        vmRepository.deleteById(id);
    }
}

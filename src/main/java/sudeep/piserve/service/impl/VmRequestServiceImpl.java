package sudeep.piserve.service.impl;

import sudeep.piserve.service.VmRequestService;
import sudeep.piserve.domain.VmRequest;
import sudeep.piserve.repository.VmRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link VmRequest}.
 */
@Service
@Transactional
public class VmRequestServiceImpl implements VmRequestService {

    private final Logger log = LoggerFactory.getLogger(VmRequestServiceImpl.class);

    private final VmRequestRepository vmRequestRepository;

    public VmRequestServiceImpl(VmRequestRepository vmRequestRepository) {
        this.vmRequestRepository = vmRequestRepository;
    }

    /**
     * Save a vmRequest.
     *
     * @param vmRequest the entity to save.
     * @return the persisted entity.
     */
    @Override
    public VmRequest save(VmRequest vmRequest) {
        log.debug("Request to save VmRequest : {}", vmRequest);
        return vmRequestRepository.save(vmRequest);
    }

    /**
     * Get all the vmRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VmRequest> findAll(Pageable pageable) {
        log.debug("Request to get all VmRequests");
        return vmRequestRepository.findAll(pageable);
    }


    /**
     * Get one vmRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VmRequest> findOne(Long id) {
        log.debug("Request to get VmRequest : {}", id);
        return vmRequestRepository.findById(id);
    }

    /**
     * Delete the vmRequest by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VmRequest : {}", id);
        vmRequestRepository.deleteById(id);
    }
}

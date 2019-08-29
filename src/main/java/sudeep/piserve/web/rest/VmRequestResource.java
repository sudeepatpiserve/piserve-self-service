package sudeep.piserve.web.rest;

import sudeep.piserve.domain.VmRequest;
import sudeep.piserve.service.VmRequestService;
import sudeep.piserve.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sudeep.piserve.domain.VmRequest}.
 */
@RestController
@RequestMapping("/api")
public class VmRequestResource {

    private final Logger log = LoggerFactory.getLogger(VmRequestResource.class);

    private static final String ENTITY_NAME = "vmRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VmRequestService vmRequestService;

    public VmRequestResource(VmRequestService vmRequestService) {
        this.vmRequestService = vmRequestService;
    }

    /**
     * {@code POST  /vm-requests} : Create a new vmRequest.
     *
     * @param vmRequest the vmRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vmRequest, or with status {@code 400 (Bad Request)} if the vmRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vm-requests")
    public ResponseEntity<VmRequest> createVmRequest(@RequestBody VmRequest vmRequest) throws URISyntaxException {
        log.debug("REST request to save VmRequest : {}", vmRequest);
        if (vmRequest.getId() != null) {
            throw new BadRequestAlertException("A new vmRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VmRequest result = vmRequestService.save(vmRequest);
        return ResponseEntity.created(new URI("/api/vm-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vm-requests} : Updates an existing vmRequest.
     *
     * @param vmRequest the vmRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vmRequest,
     * or with status {@code 400 (Bad Request)} if the vmRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vmRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vm-requests")
    public ResponseEntity<VmRequest> updateVmRequest(@RequestBody VmRequest vmRequest) throws URISyntaxException {
        log.debug("REST request to update VmRequest : {}", vmRequest);
        if (vmRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VmRequest result = vmRequestService.save(vmRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vmRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vm-requests} : get all the vmRequests.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vmRequests in body.
     */
    @GetMapping("/vm-requests")
    public ResponseEntity<List<VmRequest>> getAllVmRequests(Pageable pageable) {
        log.debug("REST request to get a page of VmRequests");
        Page<VmRequest> page = vmRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vm-requests/:id} : get the "id" vmRequest.
     *
     * @param id the id of the vmRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vmRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vm-requests/{id}")
    public ResponseEntity<VmRequest> getVmRequest(@PathVariable Long id) {
        log.debug("REST request to get VmRequest : {}", id);
        Optional<VmRequest> vmRequest = vmRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vmRequest);
    }

    /**
     * {@code DELETE  /vm-requests/:id} : delete the "id" vmRequest.
     *
     * @param id the id of the vmRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vm-requests/{id}")
    public ResponseEntity<Void> deleteVmRequest(@PathVariable Long id) {
        log.debug("REST request to delete VmRequest : {}", id);
        vmRequestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

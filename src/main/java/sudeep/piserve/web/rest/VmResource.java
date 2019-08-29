package sudeep.piserve.web.rest;

import sudeep.piserve.domain.Vm;
import sudeep.piserve.service.VmService;
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
 * REST controller for managing {@link sudeep.piserve.domain.Vm}.
 */
@RestController
@RequestMapping("/api")
public class VmResource {

    private final Logger log = LoggerFactory.getLogger(VmResource.class);

    private static final String ENTITY_NAME = "vm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VmService vmService;

    public VmResource(VmService vmService) {
        this.vmService = vmService;
    }

    /**
     * {@code POST  /vms} : Create a new vm.
     *
     * @param vm the vm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vm, or with status {@code 400 (Bad Request)} if the vm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vms")
    public ResponseEntity<Vm> createVm(@RequestBody Vm vm) throws URISyntaxException {
        log.debug("REST request to save Vm : {}", vm);
        if (vm.getId() != null) {
            throw new BadRequestAlertException("A new vm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vm result = vmService.save(vm);
        return ResponseEntity.created(new URI("/api/vms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vms} : Updates an existing vm.
     *
     * @param vm the vm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vm,
     * or with status {@code 400 (Bad Request)} if the vm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vms")
    public ResponseEntity<Vm> updateVm(@RequestBody Vm vm) throws URISyntaxException {
        log.debug("REST request to update Vm : {}", vm);
        if (vm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vm result = vmService.save(vm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vm.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vms} : get all the vms.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vms in body.
     */
    @GetMapping("/vms")
    public ResponseEntity<List<Vm>> getAllVms(Pageable pageable) {
        log.debug("REST request to get a page of Vms");
        Page<Vm> page = vmService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vms/:id} : get the "id" vm.
     *
     * @param id the id of the vm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vms/{id}")
    public ResponseEntity<Vm> getVm(@PathVariable Long id) {
        log.debug("REST request to get Vm : {}", id);
        Optional<Vm> vm = vmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vm);
    }

    /**
     * {@code DELETE  /vms/:id} : delete the "id" vm.
     *
     * @param id the id of the vm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vms/{id}")
    public ResponseEntity<Void> deleteVm(@PathVariable Long id) {
        log.debug("REST request to delete Vm : {}", id);
        vmService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

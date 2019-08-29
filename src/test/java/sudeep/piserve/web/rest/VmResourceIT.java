package sudeep.piserve.web.rest;

import sudeep.piserve.PiserveSelfServiceApp;
import sudeep.piserve.domain.Vm;
import sudeep.piserve.repository.VmRepository;
import sudeep.piserve.service.VmService;
import sudeep.piserve.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static sudeep.piserve.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VmResource} REST controller.
 */
@SpringBootTest(classes = PiserveSelfServiceApp.class)
public class VmResourceIT {

    private static final String DEFAULT_VMNAME = "AAAAAAAAAA";
    private static final String UPDATED_VMNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_CPU = 1;
    private static final Integer UPDATED_NUM_CPU = 2;
    private static final Integer SMALLER_NUM_CPU = 1 - 1;

    private static final String DEFAULT_MEMORY = "AAAAAAAAAA";
    private static final String UPDATED_MEMORY = "BBBBBBBBBB";

    @Autowired
    private VmRepository vmRepository;

    @Autowired
    private VmService vmService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVmMockMvc;

    private Vm vm;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VmResource vmResource = new VmResource(vmService);
        this.restVmMockMvc = MockMvcBuilders.standaloneSetup(vmResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vm createEntity(EntityManager em) {
        Vm vm = new Vm()
            .vmname(DEFAULT_VMNAME)
            .numCPU(DEFAULT_NUM_CPU)
            .memory(DEFAULT_MEMORY);
        return vm;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vm createUpdatedEntity(EntityManager em) {
        Vm vm = new Vm()
            .vmname(UPDATED_VMNAME)
            .numCPU(UPDATED_NUM_CPU)
            .memory(UPDATED_MEMORY);
        return vm;
    }

    @BeforeEach
    public void initTest() {
        vm = createEntity(em);
    }

    @Test
    @Transactional
    public void createVm() throws Exception {
        int databaseSizeBeforeCreate = vmRepository.findAll().size();

        // Create the Vm
        restVmMockMvc.perform(post("/api/vms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vm)))
            .andExpect(status().isCreated());

        // Validate the Vm in the database
        List<Vm> vmList = vmRepository.findAll();
        assertThat(vmList).hasSize(databaseSizeBeforeCreate + 1);
        Vm testVm = vmList.get(vmList.size() - 1);
        assertThat(testVm.getVmname()).isEqualTo(DEFAULT_VMNAME);
        assertThat(testVm.getNumCPU()).isEqualTo(DEFAULT_NUM_CPU);
        assertThat(testVm.getMemory()).isEqualTo(DEFAULT_MEMORY);
    }

    @Test
    @Transactional
    public void createVmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vmRepository.findAll().size();

        // Create the Vm with an existing ID
        vm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVmMockMvc.perform(post("/api/vms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vm)))
            .andExpect(status().isBadRequest());

        // Validate the Vm in the database
        List<Vm> vmList = vmRepository.findAll();
        assertThat(vmList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVms() throws Exception {
        // Initialize the database
        vmRepository.saveAndFlush(vm);

        // Get all the vmList
        restVmMockMvc.perform(get("/api/vms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vm.getId().intValue())))
            .andExpect(jsonPath("$.[*].vmname").value(hasItem(DEFAULT_VMNAME.toString())))
            .andExpect(jsonPath("$.[*].numCPU").value(hasItem(DEFAULT_NUM_CPU)))
            .andExpect(jsonPath("$.[*].memory").value(hasItem(DEFAULT_MEMORY.toString())));
    }
    
    @Test
    @Transactional
    public void getVm() throws Exception {
        // Initialize the database
        vmRepository.saveAndFlush(vm);

        // Get the vm
        restVmMockMvc.perform(get("/api/vms/{id}", vm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vm.getId().intValue()))
            .andExpect(jsonPath("$.vmname").value(DEFAULT_VMNAME.toString()))
            .andExpect(jsonPath("$.numCPU").value(DEFAULT_NUM_CPU))
            .andExpect(jsonPath("$.memory").value(DEFAULT_MEMORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVm() throws Exception {
        // Get the vm
        restVmMockMvc.perform(get("/api/vms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVm() throws Exception {
        // Initialize the database
        vmService.save(vm);

        int databaseSizeBeforeUpdate = vmRepository.findAll().size();

        // Update the vm
        Vm updatedVm = vmRepository.findById(vm.getId()).get();
        // Disconnect from session so that the updates on updatedVm are not directly saved in db
        em.detach(updatedVm);
        updatedVm
            .vmname(UPDATED_VMNAME)
            .numCPU(UPDATED_NUM_CPU)
            .memory(UPDATED_MEMORY);

        restVmMockMvc.perform(put("/api/vms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVm)))
            .andExpect(status().isOk());

        // Validate the Vm in the database
        List<Vm> vmList = vmRepository.findAll();
        assertThat(vmList).hasSize(databaseSizeBeforeUpdate);
        Vm testVm = vmList.get(vmList.size() - 1);
        assertThat(testVm.getVmname()).isEqualTo(UPDATED_VMNAME);
        assertThat(testVm.getNumCPU()).isEqualTo(UPDATED_NUM_CPU);
        assertThat(testVm.getMemory()).isEqualTo(UPDATED_MEMORY);
    }

    @Test
    @Transactional
    public void updateNonExistingVm() throws Exception {
        int databaseSizeBeforeUpdate = vmRepository.findAll().size();

        // Create the Vm

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVmMockMvc.perform(put("/api/vms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vm)))
            .andExpect(status().isBadRequest());

        // Validate the Vm in the database
        List<Vm> vmList = vmRepository.findAll();
        assertThat(vmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVm() throws Exception {
        // Initialize the database
        vmService.save(vm);

        int databaseSizeBeforeDelete = vmRepository.findAll().size();

        // Delete the vm
        restVmMockMvc.perform(delete("/api/vms/{id}", vm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vm> vmList = vmRepository.findAll();
        assertThat(vmList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vm.class);
        Vm vm1 = new Vm();
        vm1.setId(1L);
        Vm vm2 = new Vm();
        vm2.setId(vm1.getId());
        assertThat(vm1).isEqualTo(vm2);
        vm2.setId(2L);
        assertThat(vm1).isNotEqualTo(vm2);
        vm1.setId(null);
        assertThat(vm1).isNotEqualTo(vm2);
    }
}

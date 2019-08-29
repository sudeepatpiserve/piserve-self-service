package sudeep.piserve.web.rest;

import sudeep.piserve.PiserveSelfServiceApp;
import sudeep.piserve.domain.VmRequest;
import sudeep.piserve.repository.VmRequestRepository;
import sudeep.piserve.service.VmRequestService;
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
 * Integration tests for the {@link VmRequestResource} REST controller.
 */
@SpringBootTest(classes = PiserveSelfServiceApp.class)
public class VmRequestResourceIT {

    private static final String DEFAULT_DC = "AAAAAAAAAA";
    private static final String UPDATED_DC = "BBBBBBBBBB";

    private static final String DEFAULT_DATASTORE = "AAAAAAAAAA";
    private static final String UPDATED_DATASTORE = "BBBBBBBBBB";

    private static final String DEFAULT_CLUSTER = "AAAAAAAAAA";
    private static final String UPDATED_CLUSTER = "BBBBBBBBBB";

    private static final String DEFAULT_NETWORK = "AAAAAAAAAA";
    private static final String UPDATED_NETWORK = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE = "BBBBBBBBBB";

    @Autowired
    private VmRequestRepository vmRequestRepository;

    @Autowired
    private VmRequestService vmRequestService;

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

    private MockMvc restVmRequestMockMvc;

    private VmRequest vmRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VmRequestResource vmRequestResource = new VmRequestResource(vmRequestService);
        this.restVmRequestMockMvc = MockMvcBuilders.standaloneSetup(vmRequestResource)
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
    public static VmRequest createEntity(EntityManager em) {
        VmRequest vmRequest = new VmRequest()
            .dc(DEFAULT_DC)
            .datastore(DEFAULT_DATASTORE)
            .cluster(DEFAULT_CLUSTER)
            .network(DEFAULT_NETWORK)
            .template(DEFAULT_TEMPLATE);
        return vmRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VmRequest createUpdatedEntity(EntityManager em) {
        VmRequest vmRequest = new VmRequest()
            .dc(UPDATED_DC)
            .datastore(UPDATED_DATASTORE)
            .cluster(UPDATED_CLUSTER)
            .network(UPDATED_NETWORK)
            .template(UPDATED_TEMPLATE);
        return vmRequest;
    }

    @BeforeEach
    public void initTest() {
        vmRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createVmRequest() throws Exception {
        int databaseSizeBeforeCreate = vmRequestRepository.findAll().size();

        // Create the VmRequest
        restVmRequestMockMvc.perform(post("/api/vm-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vmRequest)))
            .andExpect(status().isCreated());

        // Validate the VmRequest in the database
        List<VmRequest> vmRequestList = vmRequestRepository.findAll();
        assertThat(vmRequestList).hasSize(databaseSizeBeforeCreate + 1);
        VmRequest testVmRequest = vmRequestList.get(vmRequestList.size() - 1);
        assertThat(testVmRequest.getDc()).isEqualTo(DEFAULT_DC);
        assertThat(testVmRequest.getDatastore()).isEqualTo(DEFAULT_DATASTORE);
        assertThat(testVmRequest.getCluster()).isEqualTo(DEFAULT_CLUSTER);
        assertThat(testVmRequest.getNetwork()).isEqualTo(DEFAULT_NETWORK);
        assertThat(testVmRequest.getTemplate()).isEqualTo(DEFAULT_TEMPLATE);
    }

    @Test
    @Transactional
    public void createVmRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vmRequestRepository.findAll().size();

        // Create the VmRequest with an existing ID
        vmRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVmRequestMockMvc.perform(post("/api/vm-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vmRequest)))
            .andExpect(status().isBadRequest());

        // Validate the VmRequest in the database
        List<VmRequest> vmRequestList = vmRequestRepository.findAll();
        assertThat(vmRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVmRequests() throws Exception {
        // Initialize the database
        vmRequestRepository.saveAndFlush(vmRequest);

        // Get all the vmRequestList
        restVmRequestMockMvc.perform(get("/api/vm-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vmRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].dc").value(hasItem(DEFAULT_DC.toString())))
            .andExpect(jsonPath("$.[*].datastore").value(hasItem(DEFAULT_DATASTORE.toString())))
            .andExpect(jsonPath("$.[*].cluster").value(hasItem(DEFAULT_CLUSTER.toString())))
            .andExpect(jsonPath("$.[*].network").value(hasItem(DEFAULT_NETWORK.toString())))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.toString())));
    }
    
    @Test
    @Transactional
    public void getVmRequest() throws Exception {
        // Initialize the database
        vmRequestRepository.saveAndFlush(vmRequest);

        // Get the vmRequest
        restVmRequestMockMvc.perform(get("/api/vm-requests/{id}", vmRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vmRequest.getId().intValue()))
            .andExpect(jsonPath("$.dc").value(DEFAULT_DC.toString()))
            .andExpect(jsonPath("$.datastore").value(DEFAULT_DATASTORE.toString()))
            .andExpect(jsonPath("$.cluster").value(DEFAULT_CLUSTER.toString()))
            .andExpect(jsonPath("$.network").value(DEFAULT_NETWORK.toString()))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVmRequest() throws Exception {
        // Get the vmRequest
        restVmRequestMockMvc.perform(get("/api/vm-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVmRequest() throws Exception {
        // Initialize the database
        vmRequestService.save(vmRequest);

        int databaseSizeBeforeUpdate = vmRequestRepository.findAll().size();

        // Update the vmRequest
        VmRequest updatedVmRequest = vmRequestRepository.findById(vmRequest.getId()).get();
        // Disconnect from session so that the updates on updatedVmRequest are not directly saved in db
        em.detach(updatedVmRequest);
        updatedVmRequest
            .dc(UPDATED_DC)
            .datastore(UPDATED_DATASTORE)
            .cluster(UPDATED_CLUSTER)
            .network(UPDATED_NETWORK)
            .template(UPDATED_TEMPLATE);

        restVmRequestMockMvc.perform(put("/api/vm-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVmRequest)))
            .andExpect(status().isOk());

        // Validate the VmRequest in the database
        List<VmRequest> vmRequestList = vmRequestRepository.findAll();
        assertThat(vmRequestList).hasSize(databaseSizeBeforeUpdate);
        VmRequest testVmRequest = vmRequestList.get(vmRequestList.size() - 1);
        assertThat(testVmRequest.getDc()).isEqualTo(UPDATED_DC);
        assertThat(testVmRequest.getDatastore()).isEqualTo(UPDATED_DATASTORE);
        assertThat(testVmRequest.getCluster()).isEqualTo(UPDATED_CLUSTER);
        assertThat(testVmRequest.getNetwork()).isEqualTo(UPDATED_NETWORK);
        assertThat(testVmRequest.getTemplate()).isEqualTo(UPDATED_TEMPLATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVmRequest() throws Exception {
        int databaseSizeBeforeUpdate = vmRequestRepository.findAll().size();

        // Create the VmRequest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVmRequestMockMvc.perform(put("/api/vm-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vmRequest)))
            .andExpect(status().isBadRequest());

        // Validate the VmRequest in the database
        List<VmRequest> vmRequestList = vmRequestRepository.findAll();
        assertThat(vmRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVmRequest() throws Exception {
        // Initialize the database
        vmRequestService.save(vmRequest);

        int databaseSizeBeforeDelete = vmRequestRepository.findAll().size();

        // Delete the vmRequest
        restVmRequestMockMvc.perform(delete("/api/vm-requests/{id}", vmRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VmRequest> vmRequestList = vmRequestRepository.findAll();
        assertThat(vmRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VmRequest.class);
        VmRequest vmRequest1 = new VmRequest();
        vmRequest1.setId(1L);
        VmRequest vmRequest2 = new VmRequest();
        vmRequest2.setId(vmRequest1.getId());
        assertThat(vmRequest1).isEqualTo(vmRequest2);
        vmRequest2.setId(2L);
        assertThat(vmRequest1).isNotEqualTo(vmRequest2);
        vmRequest1.setId(null);
        assertThat(vmRequest1).isNotEqualTo(vmRequest2);
    }
}

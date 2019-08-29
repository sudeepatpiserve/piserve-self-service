package sudeep.piserve.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A VmRequest.
 */
@Entity
@Table(name = "vm_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VmRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "dc")
    private String dc;

    @Column(name = "datastore")
    private String datastore;

    @Column(name = "cluster")
    private String cluster;

    @Column(name = "network")
    private String network;

    @Column(name = "template")
    private String template;

    @ManyToOne
    @JsonIgnoreProperties("vmRequests")
    private Vm vm;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDc() {
        return dc;
    }

    public VmRequest dc(String dc) {
        this.dc = dc;
        return this;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getDatastore() {
        return datastore;
    }

    public VmRequest datastore(String datastore) {
        this.datastore = datastore;
        return this;
    }

    public void setDatastore(String datastore) {
        this.datastore = datastore;
    }

    public String getCluster() {
        return cluster;
    }

    public VmRequest cluster(String cluster) {
        this.cluster = cluster;
        return this;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getNetwork() {
        return network;
    }

    public VmRequest network(String network) {
        this.network = network;
        return this;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getTemplate() {
        return template;
    }

    public VmRequest template(String template) {
        this.template = template;
        return this;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Vm getVm() {
        return vm;
    }

    public VmRequest vm(Vm vm) {
        this.vm = vm;
        return this;
    }

    public void setVm(Vm vm) {
        this.vm = vm;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VmRequest)) {
            return false;
        }
        return id != null && id.equals(((VmRequest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "VmRequest{" +
            "id=" + getId() +
            ", dc='" + getDc() + "'" +
            ", datastore='" + getDatastore() + "'" +
            ", cluster='" + getCluster() + "'" +
            ", network='" + getNetwork() + "'" +
            ", template='" + getTemplate() + "'" +
            "}";
    }
}

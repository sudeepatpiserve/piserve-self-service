package sudeep.piserve.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vm.
 */
@Entity
@Table(name = "vm")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "vmname")
    private String vmname;

    @Column(name = "num_cpu")
    private Integer numCPU;

    @Column(name = "memory")
    private String memory;

    @OneToMany(mappedBy = "vm")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VmRequest> vmRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVmname() {
        return vmname;
    }

    public Vm vmname(String vmname) {
        this.vmname = vmname;
        return this;
    }

    public void setVmname(String vmname) {
        this.vmname = vmname;
    }

    public Integer getNumCPU() {
        return numCPU;
    }

    public Vm numCPU(Integer numCPU) {
        this.numCPU = numCPU;
        return this;
    }

    public void setNumCPU(Integer numCPU) {
        this.numCPU = numCPU;
    }

    public String getMemory() {
        return memory;
    }

    public Vm memory(String memory) {
        this.memory = memory;
        return this;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public Set<VmRequest> getVmRequests() {
        return vmRequests;
    }

    public Vm vmRequests(Set<VmRequest> vmRequests) {
        this.vmRequests = vmRequests;
        return this;
    }

    public Vm addVmRequest(VmRequest vmRequest) {
        this.vmRequests.add(vmRequest);
        vmRequest.setVm(this);
        return this;
    }

    public Vm removeVmRequest(VmRequest vmRequest) {
        this.vmRequests.remove(vmRequest);
        vmRequest.setVm(null);
        return this;
    }

    public void setVmRequests(Set<VmRequest> vmRequests) {
        this.vmRequests = vmRequests;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vm)) {
            return false;
        }
        return id != null && id.equals(((Vm) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Vm{" +
            "id=" + getId() +
            ", vmname='" + getVmname() + "'" +
            ", numCPU=" + getNumCPU() +
            ", memory='" + getMemory() + "'" +
            "}";
    }
}

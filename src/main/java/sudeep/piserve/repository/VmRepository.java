package sudeep.piserve.repository;

import sudeep.piserve.domain.Vm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Vm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VmRepository extends JpaRepository<Vm, Long> {

}

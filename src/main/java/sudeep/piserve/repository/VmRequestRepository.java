package sudeep.piserve.repository;

import sudeep.piserve.domain.VmRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VmRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VmRequestRepository extends JpaRepository<VmRequest, Long> {

}

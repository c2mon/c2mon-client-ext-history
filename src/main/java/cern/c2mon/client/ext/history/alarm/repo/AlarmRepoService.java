package cern.c2mon.client.ext.history.alarm.repo;

import cern.c2mon.client.ext.history.alarm.AlarmRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AlarmRepoService extends JpaRepository<AlarmRecord, Long> {

    /**
     * Find AlarmRecord by Fault Family
     * @param faultFamily
     * @return
     */
    List<AlarmRecord> findFirst10ByFaultFamilyContainingIgnoreCase(@Param("faultFamily") String faultFamily);

    /**
     * Find AlarmRecord by Fault Member
     * @param faultMember
     * @return
     */
    List<AlarmRecord> findFirst10ByFaultMemberContainingIgnoreCase(@Param("faultMember") String faultMember);

    /**
     * Find AlarmRecord by Fault Code
     * @param faultCode
     * @return
     */
    List<AlarmRecord> findFirst10ByFaultCode(@Param("faultCode") Integer faultCode);

    /**
     * Find AlarmRecord by Fault Family and Fault Member
     * @param faultFamily
     * @param faultMember
     * @return
     */
    List<AlarmRecord> findFirst10ByFaultFamilyContainingIgnoreCaseAndFaultMemberContainingIgnoreCase(@Param("faultFamily") String faultFamily,
                                                                                              @Param("faultMember") String faultMember);

    /**
     * Find AlarmRecord by Fault Family and Fault Code
     * @param faultFamily
     * @param faultCode
     * @return
     */
    List<AlarmRecord> findFirst10ByFaultFamilyContainingIgnoreCaseAndFaultCode(@Param("faultFamily") String faultFamily,
                                                                       @Param("faultCode") Integer faultCode);

    /**
     * Find AlarmRecord by Fault Member and Fault Code
     * @param faultMember
     * @param faultCode
     * @return
     */
    List<AlarmRecord> findFirst10ByFaultMemberContainingIgnoreCaseAndFaultCode(@Param("faultMember") String faultMember,
                                                                        @Param("faultCode") Integer faultCode);

    /**
     * Find AlarmRecord by Fault Family, Fault Member and FaultCode
     * @param faultFamily
     * @param faultMember
     * @param faultCode
     * @return
     */
    List<AlarmRecord> findFirst10ByFaultFamilyContainingIgnoreCaseAndFaultMemberContainingIgnoreCaseAndFaultCode(@Param("faultFamily") String faultFamily,
                                                                                                          @Param("faultMember") String faultMember,
                                                                                                          @Param("faultCode") Integer faultCode);


    List<AlarmRecord> findByTagId(Long tagId);

    Optional<AlarmRecord> findById(Long alarmId);
}

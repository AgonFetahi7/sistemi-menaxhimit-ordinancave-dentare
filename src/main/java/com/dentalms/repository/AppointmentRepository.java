package com.dentalms.repository;

import com.dentalms.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Rregulli i perplasjes: dy intervale [s1,e1) dhe [s2,e2) perplasen kur s1 < e2 dhe e1 > s2.
     * Numerohen vetem terminet aktive (SCHEDULED) te se njejtit dentist; excludeId perjashton
     * terminin qe po ndryshohet (per create jepet -1).
     */
    @Query("""
           select count(a) > 0 from Appointment a
           where a.dentist.id = :dentistId
             and a.status = com.dentalms.model.AppointmentStatus.SCHEDULED
             and a.id <> :excludeId
             and a.startAt < :endAt
             and a.endAt > :startAt
           """)
    boolean existsOverlap(@Param("dentistId") Long dentistId,
                          @Param("startAt") LocalDateTime startAt,
                          @Param("endAt") LocalDateTime endAt,
                          @Param("excludeId") Long excludeId);

    /** Filtrim sipas intervalit kohor dhe (opsionalisht) dentistit. */
    @Query("""
           select a from Appointment a
           where a.startAt >= :from and a.startAt < :to
             and (:dentistId is null or a.dentist.id = :dentistId)
           order by a.startAt
           """)
    List<Appointment> search(@Param("from") LocalDateTime from,
                             @Param("to") LocalDateTime to,
                             @Param("dentistId") Long dentistId);
}

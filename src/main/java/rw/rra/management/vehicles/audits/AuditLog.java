package rw.rra.management.vehicles.audits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String action;
    private String username;
    private String details;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

}

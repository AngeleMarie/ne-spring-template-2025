package rw.rra.management.vehicles.audits;

import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(value = {"createdBy", "updatedBy"}, allowGetters = true)
public abstract class InitiatorAudit extends TimestampAudit{
    private static final Long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "created_by")
    private UUID createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private UUID updatedBy;
}

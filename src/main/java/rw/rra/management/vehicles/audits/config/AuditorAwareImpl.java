package rw.rra.management.vehicles.audits.config;


import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        // Replace this with authenticated user UUID logic
        return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }
}

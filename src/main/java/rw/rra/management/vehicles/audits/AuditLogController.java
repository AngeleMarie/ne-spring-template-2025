package rw.rra.management.vehicles.audits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audits")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/get-all")
    public List<AuditLog> getAllLogs() {
        return auditLogService.getAllLogs();
    }

    @GetMapping("/user/{username}")
    public List<AuditLog> getLogsByUser(@PathVariable String username) {
        return auditLogService.getLogsByUser(username);
    }
}

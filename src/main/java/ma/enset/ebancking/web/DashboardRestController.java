package ma.enset.ebancking.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebancking.dtos.DashboardStatsDTO;
import ma.enset.ebancking.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class DashboardRestController {
    
    private DashboardService dashboardService;
    
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        try {
            DashboardStatsDTO stats = dashboardService.getDashboardStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error getting dashboard statistics", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/stats/{period}")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@PathVariable String period) {
        try {
            DashboardStatsDTO stats = dashboardService.getDashboardStatistics(period);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error getting dashboard statistics for period: " + period, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

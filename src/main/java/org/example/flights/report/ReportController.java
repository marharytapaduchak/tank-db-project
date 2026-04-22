package org.example.flights.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;

    @GetMapping("/tank/report/{tankId}")
    public String reportMenu(@PathVariable Integer tankId, Model model) {
        model.addAttribute("tankId", tankId);
        return "report/menu";
    }

    @GetMapping("/tank/report/assignment/{tankId}")
    public String assignmentHistory(@PathVariable Integer tankId, Model model) {
        model.addAttribute("tankId", tankId);
        model.addAttribute("rows", reportRepository.getAssignmentHistory(tankId));
        return "report/assignment-history";
    }

    @GetMapping("/tank/report/maintenance/{tankId}")
    public String maintenanceHistory(@PathVariable Integer tankId, Model model) {
        model.addAttribute("tankId", tankId);
        model.addAttribute("rows", reportRepository.getMaintenanceHistory(tankId));
        return "report/maintenance-history";
    }

    @GetMapping("/tank/report/readiness/{tankId}")
    public String readinessOverview(@PathVariable Integer tankId, Model model) {
        model.addAttribute("tankId", tankId);
        model.addAttribute("rows", reportRepository.getReadinessOverview(tankId));
        return "report/readiness-overview";
    }
}
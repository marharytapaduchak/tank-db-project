package org.example.flights.tankincident;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/tankincident")
@RequiredArgsConstructor
public class TankIncidentController {

    private final TankIncidentRepository tankIncidentRepository;

    @GetMapping
    public String list(Model model, @ModelAttribute("successMessage") String successMessage) {
        model.addAttribute("incidents", tankIncidentRepository.findAllOrdered());
        return "tankincident/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("incident", new TankIncident(
                null,
                null,
                LocalDate.now(),
                "",
                "",
                ""
        ));
        model.addAttribute("pageTitle", "Add Tank Incident");
        model.addAttribute("formAction", "/tankincident/create");
        model.addAttribute("submitLabel", "Create Incident");
        return "tankincident/create";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam Integer tankId,
            @RequestParam LocalDate incidentDate,
            @RequestParam String incidentType,
            @RequestParam(required = false) String description,
            @RequestParam String severityLevel,
            RedirectAttributes redirectAttributes
    ) {
        validate(tankId, incidentDate, incidentType, severityLevel);

        Integer nextId = tankIncidentRepository.nextId();

        String cleanedDescription = description != null ? description.trim() : null;
        String cleanedIncidentType = incidentType.trim();
        String cleanedSeverityLevel = severityLevel.trim();

        String executedQuery = String.format(
                "INSERT INTO TankIncident (IncidentID, TankID, IncidentDate, IncidentType, Description, SeverityLevel) VALUES (%d, %d, '%s', '%s', '%s', '%s');",
                nextId,
                tankId,
                incidentDate,
                cleanedIncidentType,
                cleanedDescription != null ? cleanedDescription : "",
                cleanedSeverityLevel
        );
        System.out.println("Executed query: " + executedQuery);

        tankIncidentRepository.insertIncident(
                nextId,
                tankId,
                incidentDate,
                cleanedIncidentType,
                cleanedDescription,
                cleanedSeverityLevel
        );

        redirectAttributes.addFlashAttribute("successMessage", "Tank incident was created successfully.");
        return "redirect:/tankincident";
    }

    @GetMapping("/edit/{incidentId}")
    public String editForm(@PathVariable Integer incidentId, Model model) {
        TankIncident incident = tankIncidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Incident not found."));

        model.addAttribute("incident", incident);
        model.addAttribute("pageTitle", "Edit Tank Incident");
        model.addAttribute("formAction", "/tankincident/edit/" + incidentId);
        model.addAttribute("submitLabel", "Update Incident");
        return "tankincident/edit";
    }

    @PostMapping("/edit/{incidentId}")
    public String update(
            @PathVariable Integer incidentId,
            @RequestParam Integer tankId,
            @RequestParam LocalDate incidentDate,
            @RequestParam String incidentType,
            @RequestParam(required = false) String description,
            @RequestParam String severityLevel,
            RedirectAttributes redirectAttributes
    ) {
        TankIncident existing = tankIncidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Incident not found."));

        validate(tankId, incidentDate, incidentType, severityLevel);

        TankIncident updated = new TankIncident(
                existing.incidentId(),
                tankId,
                incidentDate,
                incidentType.trim(),
                description != null ? description.trim() : null,
                severityLevel.trim()
        );

        String executedQuery = String.format(
                "UPDATE TankIncident SET TankID = %d, IncidentDate = '%s', IncidentType = '%s', Description = '%s', SeverityLevel = '%s' WHERE IncidentID = %d;",
                tankId,
                incidentDate,
                incidentType.trim(),
                description != null ? description.trim() : "",
                severityLevel.trim(),
                incidentId
        );
        System.out.println("Executed query: " + executedQuery);

        tankIncidentRepository.save(updated);
        redirectAttributes.addFlashAttribute("successMessage", "Tank incident was updated successfully.");
        return "redirect:/tankincident";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        tankIncidentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Incident not found."));

        String executedQuery = String.format(
                "DELETE FROM TankIncident WHERE IncidentID = %d;",
                id
        );
        System.out.println("Executed query: " + executedQuery);

        tankIncidentRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Tank incident was deleted successfully.");
        return "redirect:/tankincident";
    }

    private void validate(Integer tankId, LocalDate incidentDate, String incidentType, String severityLevel) {
        if (tankId == null || tankId <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Tank ID must be a positive integer.");
        }
        if (incidentDate == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Incident date is required.");
        }
        if (incidentType == null || incidentType.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Incident type is required.");
        }
        if (severityLevel == null || severityLevel.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Severity level is required.");
        }
    }
}

package org.example.flights.tankassignment;

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
@RequestMapping("/tankassignment")
@RequiredArgsConstructor
public class TankAssignmentController {

    private final TankAssignmentRepository tankAssignmentRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("assignments", tankAssignmentRepository.findAllOrdered());
        return "tankassignment/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("assignment", new TankAssignment(
                null,
                null,
                LocalDate.now(),
                null
        ));
        model.addAttribute("pageTitle", "Add Tank Assignment");
        model.addAttribute("formAction", "/tankassignment/create");
        model.addAttribute("submitLabel", "Create Assignment");
        return "tankassignment/create";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam Integer tankId,
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            RedirectAttributes redirectAttributes
    ) {
        validate(tankId, startDate, endDate);

        Integer nextId = tankAssignmentRepository.nextId();

        String executedQuery = String.format(
                "INSERT INTO TankAssignment (AssignmentID, TankID, StartDate, EndDate) VALUES (%d, %d, '%s', %s);",
                nextId,
                tankId,
                startDate,
                endDate != null ? "'" + endDate + "'" : "NULL"
        );
        System.out.println("Executed query: " + executedQuery);

        tankAssignmentRepository.insertAssignment(nextId, tankId, startDate, endDate);

        redirectAttributes.addFlashAttribute("successMessage", "Tank assignment was created successfully.");
        return "redirect:/tankassignment";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        TankAssignment assignment = tankAssignmentRepository.findByAssignmentId(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Assignment not found."));

        model.addAttribute("assignment", assignment);
        model.addAttribute("pageTitle", "Edit Tank Assignment");
        model.addAttribute("formAction", "/tankassignment/edit/" + id);
        model.addAttribute("submitLabel", "Update Assignment");
        return "tankassignment/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @RequestParam Integer tankId,
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            RedirectAttributes redirectAttributes
    ) {
        tankAssignmentRepository.findByAssignmentId(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Assignment not found."));

        validate(tankId, startDate, endDate);

        TankAssignment updated = new TankAssignment(
                id,
                tankId,
                startDate,
                endDate
        );

        String executedQuery = String.format(
                "UPDATE TankAssignment SET TankID = %d, StartDate = '%s', EndDate = %s WHERE AssignmentID = %d;",
                tankId,
                startDate,
                endDate != null ? "'" + endDate + "'" : "NULL",
                id
        );
        System.out.println("Executed query: " + executedQuery);

        tankAssignmentRepository.save(updated);

        redirectAttributes.addFlashAttribute("successMessage", "Tank assignment was updated successfully.");
        return "redirect:/tankassignment";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        tankAssignmentRepository.findByAssignmentId(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Assignment not found."));

        String executedQuery = String.format(
                "DELETE FROM TankAssignment WHERE AssignmentID = %d;",
                id
        );
        System.out.println("Executed query: " + executedQuery);

        tankAssignmentRepository.deleteAssignmentById(id);

        redirectAttributes.addFlashAttribute("successMessage", "Tank assignment was deleted successfully.");
        return "redirect:/tankassignment";
    }

    private void validate(Integer tankId, LocalDate startDate, LocalDate endDate) {
        if (tankId == null || tankId <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Tank ID must be a positive integer.");
        }
        if (startDate == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Start date is required.");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new ResponseStatusException(BAD_REQUEST, "End date cannot be earlier than start date.");
        }
    }
}
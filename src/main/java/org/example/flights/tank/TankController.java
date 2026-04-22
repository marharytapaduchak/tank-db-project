package org.example.flights.tank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/tank")
@RequiredArgsConstructor
public class TankController {

    private final TankRepository tankRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tanks", tankRepository.findAllOrdered());
        return "tank/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("tank", new Tank(
                null,
                "",
                null,
                "",
                "",
                "",
                "",
                null
        ));
        model.addAttribute("pageTitle", "Add Tank");
        model.addAttribute("formAction", "/tank/create");
        model.addAttribute("submitLabel", "Create Tank");
        return "tank/create";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam String factorySerialNumber,
            @RequestParam Integer productionYear,
            @RequestParam String currentBaseLocation,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String facilityName,
            @RequestParam Integer modelId,
            RedirectAttributes redirectAttributes
    ) {
        validate(factorySerialNumber, productionYear, currentBaseLocation, country, city, facilityName, modelId);

        Integer nextId = tankRepository.nextId();

        String cleanedSerial = factorySerialNumber.trim();
        String cleanedBase = currentBaseLocation.trim();
        String cleanedCountry = country.trim();
        String cleanedCity = city.trim();
        String cleanedFacility = facilityName.trim();

        String executedQuery = String.format(
                "INSERT INTO Tank (TankID, FactorySerialNumber, ProductionYear, CurrentBaseLocation, Country, City, FacilityName, ModelID) VALUES (%d, '%s', %d, '%s', '%s', '%s', '%s', %d);",
                nextId,
                cleanedSerial,
                productionYear,
                cleanedBase,
                cleanedCountry,
                cleanedCity,
                cleanedFacility,
                modelId
        );
        System.out.println("Executed query: " + executedQuery);

        tankRepository.insertTank(
                nextId,
                cleanedSerial,
                productionYear,
                cleanedBase,
                cleanedCountry,
                cleanedCity,
                cleanedFacility,
                modelId
        );

        redirectAttributes.addFlashAttribute("successMessage", "Tank was created successfully.");
        return "redirect:/tank";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        Tank tank = tankRepository.findByTankId(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tank not found."));

        model.addAttribute("tank", tank);
        model.addAttribute("pageTitle", "Edit Tank");
        model.addAttribute("formAction", "/tank/edit/" + id);
        model.addAttribute("submitLabel", "Update Tank");
        return "tank/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @RequestParam String factorySerialNumber,
            @RequestParam Integer productionYear,
            @RequestParam String currentBaseLocation,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String facilityName,
            @RequestParam Integer modelId,
            RedirectAttributes redirectAttributes
    ) {
        tankRepository.findByTankId(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tank not found."));

        validate(factorySerialNumber, productionYear, currentBaseLocation, country, city, facilityName, modelId);

        String cleanedSerial = factorySerialNumber.trim();
        String cleanedBase = currentBaseLocation.trim();
        String cleanedCountry = country.trim();
        String cleanedCity = city.trim();
        String cleanedFacility = facilityName.trim();

        Tank updated = new Tank(
                id,
                cleanedSerial,
                productionYear,
                cleanedBase,
                cleanedCountry,
                cleanedCity,
                cleanedFacility,
                modelId
        );

        String executedQuery = String.format(
                "UPDATE Tank SET FactorySerialNumber = '%s', ProductionYear = %d, CurrentBaseLocation = '%s', Country = '%s', City = '%s', FacilityName = '%s', ModelID = %d WHERE TankID = %d;",
                cleanedSerial,
                productionYear,
                cleanedBase,
                cleanedCountry,
                cleanedCity,
                cleanedFacility,
                modelId,
                id
        );
        System.out.println("Executed query: " + executedQuery);

        tankRepository.save(updated);

        redirectAttributes.addFlashAttribute("successMessage", "Tank was updated successfully.");
        return "redirect:/tank";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        tankRepository.findByTankId(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tank not found."));

        String executedQuery = String.format(
                "DELETE FROM Tank WHERE TankID = %d;",
                id
        );
        System.out.println("Executed query: " + executedQuery);

        tankRepository.deleteTankById(id);

        redirectAttributes.addFlashAttribute("successMessage", "Tank was deleted successfully.");
        return "redirect:/tank";
    }

    private void validate(
            String factorySerialNumber,
            Integer productionYear,
            String currentBaseLocation,
            String country,
            String city,
            String facilityName,
            Integer modelId
    ) {
        if (factorySerialNumber == null || factorySerialNumber.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Factory serial number is required.");
        }
        if (productionYear == null || productionYear < 1900 || productionYear > 2100) {
            throw new ResponseStatusException(BAD_REQUEST, "Production year is invalid.");
        }
        if (currentBaseLocation == null || currentBaseLocation.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Current base location is required.");
        }
        if (country == null || country.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Country is required.");
        }
        if (city == null || city.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "City is required.");
        }
        if (facilityName == null || facilityName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Facility name is required.");
        }
        if (modelId == null || modelId <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Model ID must be a positive integer.");
        }
    }
}
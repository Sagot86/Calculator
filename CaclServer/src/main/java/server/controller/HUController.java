package server.controller;

import org.springframework.web.bind.annotation.*;
import server.model.HistoryUnit;
import server.service.HistoryUnitService;

import java.util.List;

@RestController
@RequestMapping(path = "calc")
public class HUController {

    private HistoryUnitService historyUnitService = new HistoryUnitService();

    @GetMapping()
    public List<HistoryUnit> getRecords() {
        return historyUnitService.getLastFifteen();
    }

    @PostMapping()
    public void saveRecords(@RequestBody List<HistoryUnit> historyUnitList) {
        historyUnitService.save(historyUnitList);
    }
}
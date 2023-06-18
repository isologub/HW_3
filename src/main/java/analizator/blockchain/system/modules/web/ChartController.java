package analizator.blockchain.system.modules.web;

import analizator.blockchain.system.modules.web.model.ChartView;
import analizator.blockchain.system.modules.analizator.EthAnalizator;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class ChartController {

    private final EthAnalizator ethAnalizator;

    @GetMapping("/chart")
    public ChartView getChart() {
        return ethAnalizator.getChart();
    }
}
package analizator.blockchain.system.modules.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChartView {

    private List<String> averagePeriods;
    private List<String> averageTransactionsSize;

    private List<String> todayPeriods;
    private List<String> todayTransactionsSize;

}

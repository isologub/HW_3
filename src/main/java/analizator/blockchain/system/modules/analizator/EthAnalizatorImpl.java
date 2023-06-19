package analizator.blockchain.system.modules.analizator;

import analizator.blockchain.system.modules.repository.TransactionRepository;
import analizator.blockchain.system.modules.repository.model.EthTransactionData;
import analizator.blockchain.system.modules.web.model.ChartView;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class EthAnalizatorImpl implements EthAnalizator {

    private static final int PERCENTAGE_DIFF = 30;

    private final TransactionRepository transactionRepository;

    @Override
    public ChartView getChart() {

        List<EthTransactionData> allTransactions = transactionRepository.findAll();
        Map<String, Double> averageTransactions = getTransactionsSizeByPeriod(allTransactions);

        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime startOfDay = currentDateTime.with(LocalTime.MIN);
        ZonedDateTime endOfDay = currentDateTime.with(LocalTime.MAX);

        List<EthTransactionData> dataForCurrentDay = transactionRepository.findEntitiesForCurrentDay(startOfDay.toInstant().toEpochMilli(), endOfDay.toInstant().toEpochMilli());

        Map<String, Double> transactionsSizeByPeriodForToday = getTransactionsSizeByPeriod(dataForCurrentDay);

        Pair<List<String>, List<String>> sortedAverageTransactions = getSortedData(averageTransactions);
        Pair<List<String>, List<String>> sortedTransactionsSizeByPeriodForToday = getSortedData(transactionsSizeByPeriodForToday);

        boolean blockChainStatus = getBlockChainStatus(averageTransactions, transactionsSizeByPeriodForToday);

        return ChartView.builder()
                .averagePeriods(sortedAverageTransactions.getFirst())
                .averageTransactionsSize(sortedAverageTransactions.getSecond())
                .todayPeriods(sortedTransactionsSizeByPeriodForToday.getFirst())
                .todayTransactionsSize(sortedTransactionsSizeByPeriodForToday.getSecond())
                .blockChainStatus(blockChainStatus)
                .build();
    }

    private boolean getBlockChainStatus(Map<String, Double> averageTransactions, Map<String, Double> transactionsSizeByPeriodForToday) {
        return transactionsSizeByPeriodForToday.entrySet().stream()
                .filter(e -> Objects.nonNull(averageTransactions.get(e.getKey())))
                .allMatch((e) -> getBlockChainStatus(e.getValue(), averageTransactions.get(e.getKey())));
    }

    private static Map<String, Double> getTransactionsSizeByPeriod(List<EthTransactionData> transactions) {
        return transactions.stream()
                .collect(groupingBy(EthTransactionData::getPeriod,
                        collectingAndThen(toList(), l -> l.stream()
                                .collect(averagingInt(EthTransactionData::getTransactionsSize)))));
    }

    private Pair<List<String>, List<String>> getSortedData(Map<String, Double> unsortedData) {

        List<String> keys = new ArrayList<>(unsortedData.keySet());
        Collections.sort(keys);

        List<Double> values = new ArrayList<>();
        for (String key : keys) {
            values.add(unsortedData.get(key));
        }

        return Pair.of(keys, values.stream()
                .map(Object::toString)
                .collect(toList()));
    }

    private boolean getBlockChainStatus(Double todayTransactionSize, Double averageTransactionSize) {
        double deviation = todayTransactionSize - averageTransactionSize;
        double percentageIncrease = (Math.abs(deviation) / averageTransactionSize) * 100;

        return !(percentageIncrease > PERCENTAGE_DIFF);
    }
}

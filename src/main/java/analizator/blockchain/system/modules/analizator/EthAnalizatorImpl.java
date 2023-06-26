package analizator.blockchain.system.modules.analizator;

import analizator.blockchain.system.modules.repository.TransactionRepository;
import analizator.blockchain.system.modules.repository.model.EthTransactionData;
import analizator.blockchain.system.modules.web.model.ChartView;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

    private final TransactionRepository transactionRepository;

    @Override
    public ChartView getChart() {

        List<EthTransactionData> allTransactions = transactionRepository.findAll();
        Map<String, Double> averageTransactions = getTransactionsAverageSizeByPeriod(allTransactions);

        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime startOfDay = currentDateTime.with(LocalTime.MIN);
        ZonedDateTime endOfDay = currentDateTime.with(LocalTime.MAX);

        List<EthTransactionData> dataForCurrentDay = transactionRepository.findEntitiesForCurrentDay(startOfDay.toInstant().toEpochMilli(), endOfDay.toInstant().toEpochMilli());

        Map<String, Double> transactionsSizeByPeriodForToday = getTransactionsAverageSizeByPeriod(dataForCurrentDay);

        Map<String, List<Integer>> transactionsSizeByPeriod = getTransactionsSizeByPeriod(allTransactions);
        Map<String, Double> standardDeviationsByPeriod = getStandardDeviationsByPeriod(averageTransactions, transactionsSizeByPeriod);

        Pair<List<String>, List<String>> sortedAverageTransactions = getSortedData(averageTransactions);
        Pair<List<String>, List<String>> sortedTransactionsSizeByPeriodForToday = getSortedData(transactionsSizeByPeriodForToday);
        Pair<List<String>, List<String>> sortedStandardDeviationsForPeriods = getSortedData(standardDeviationsByPeriod);

        boolean blockChainStatus = getBlockChainStatus(averageTransactions, transactionsSizeByPeriodForToday, standardDeviationsByPeriod);

        return ChartView.builder()
                .averagePeriods(sortedAverageTransactions.getFirst())
                .averageTransactionsSize(sortedAverageTransactions.getSecond())
                .todayPeriods(sortedTransactionsSizeByPeriodForToday.getFirst())
                .todayTransactionsSize(sortedTransactionsSizeByPeriodForToday.getSecond())
                .blockChainStatus(blockChainStatus)
                .standardDeviationsPeriods(sortedStandardDeviationsForPeriods.getFirst())
                .standardDeviations(sortedStandardDeviationsForPeriods.getSecond())
                .build();
    }

    @NotNull
    private static Map<String, Double> getStandardDeviationsByPeriod(Map<String, Double> averageTransactions, Map<String, List<Integer>> transactionsSizeByPeriod) {
        return transactionsSizeByPeriod.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> {
                    String period = e.getKey();
                    Double transactionSizeForPeriod = averageTransactions.get(period);
                    Double collect = e.getValue().stream().map(transactionSizeInBlock -> Math.pow(transactionSizeInBlock - transactionSizeForPeriod, 2)).collect(averagingDouble(Double::doubleValue));
                    return Math.sqrt(collect);
                }));
    }

    private boolean getBlockChainStatus(Map<String, Double> averageTransactions, Map<String, Double> transactionsSizeByPeriodForToday, Map<String, Double> standardDeviationsByPeriod) {
        return transactionsSizeByPeriodForToday.entrySet().stream()
                .filter(e -> Objects.nonNull(averageTransactions.get(e.getKey())))
                .allMatch((e) -> getBlockChainStatus(e.getValue(), averageTransactions.get(e.getKey()), standardDeviationsByPeriod.get(e.getKey())));
    }

    private static Map<String, Double> getTransactionsAverageSizeByPeriod(List<EthTransactionData> transactions) {
        return transactions.stream()
                .collect(groupingBy(EthTransactionData::getPeriod,
                        collectingAndThen(toList(), l -> l.stream()
                                .collect(averagingInt(EthTransactionData::getTransactionsSize)))));
    }

    private static Map<String, List<Integer>> getTransactionsSizeByPeriod(List<EthTransactionData> transactions) {
        return transactions.stream()
                .collect(groupingBy(EthTransactionData::getPeriod, collectingAndThen(toList(), l -> l.stream()
                        .map(EthTransactionData::getTransactionsSize)
                        .collect(toList()))));
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

    private boolean getBlockChainStatus(Double todayTransactionSize, Double averageTransactionSize, Double standardDeviationsForThisPeriod) {
        double deviation = todayTransactionSize - averageTransactionSize;
        double percentageIncrease = (Math.abs(deviation) / averageTransactionSize) * 100;

        return !(percentageIncrease > standardDeviationsForThisPeriod);
    }
}

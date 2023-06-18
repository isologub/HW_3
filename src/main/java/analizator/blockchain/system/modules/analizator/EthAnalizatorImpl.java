package analizator.blockchain.system.modules.analizator;

import analizator.blockchain.system.modules.EthTransactionData;
import analizator.blockchain.system.modules.repository.TransactionRepository;
import analizator.blockchain.system.modules.web.model.ChartView;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class EthAnalizatorImpl implements EthAnalizator {
    private final TransactionRepository transactionRepository;

    @Override
    public ChartView getChart() {

        List<EthTransactionData> allTransactions = transactionRepository.findAll();
        Map<String, Double> averageTransactions = getTransactionsSizeByPeriod(allTransactions);

        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime startOfDay = currentDateTime.with(LocalTime.MIN);
        ZonedDateTime endOfDay = currentDateTime.with(LocalTime.MAX);

        List<EthTransactionData> entitiesForCurrentDay = transactionRepository.findEntitiesForCurrentDay(startOfDay.toInstant().toEpochMilli(), endOfDay.toInstant().toEpochMilli());

        Map<String, Double> transactionsSizeByPeriodForToday = getTransactionsSizeByPeriod(entitiesForCurrentDay);

        return ChartView.builder()
                .averagePeriods(new ArrayList<>(averageTransactions.keySet()))
                .averageTransactionsSize(averageTransactions.values().stream().map(Object::toString).collect(toList()))
                .todayPeriods(new ArrayList<>(transactionsSizeByPeriodForToday.keySet()))
                .todayTransactionsSize(transactionsSizeByPeriodForToday.values().stream().map(Object::toString).collect(toList()))
                .build();
    }

    @NotNull
    private static Map<String, Double> getTransactionsSizeByPeriod(List<EthTransactionData> all) {
        return all.stream()
                .collect(groupingBy(EthTransactionData::getPeriod,
                        collectingAndThen(toList(), l -> l.stream()
                                .collect(averagingInt(EthTransactionData::getTransactionsSize)))));
    }
}

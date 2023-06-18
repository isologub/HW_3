package analizator.blockchain.system.modules.connector;

import analizator.blockchain.system.modules.repository.model.EthTransactionData;
import analizator.blockchain.system.modules.repository.TransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class EthBlockChainConnectorService {

    public static final String PROJECT_ID = "2a38e429223840d09f608a6c51129315";
    public static final String MAINNET_INFURA_IO_V_3_URL = "https://mainnet.infura.io/v3/" + PROJECT_ID;

    private final Web3j web3j;
    private final TransactionRepository transactionRepository;
    private BigInteger startBlock;

    public EthBlockChainConnectorService(TransactionRepository transactionRepository) throws IOException {
        web3j = Web3j.build(new HttpService(MAINNET_INFURA_IO_V_3_URL));
        startBlock = web3j.ethBlockNumber().send().getBlockNumber();
        startBlock = startBlock.subtract(BigInteger.valueOf(20000));
        this.transactionRepository = transactionRepository;

    }

    @Scheduled(fixedDelay = 60_000)
//    @Scheduled(fixedDelay = 60_000)
    public void getTransactionCount() throws IOException {
        BigInteger nextBlock = web3j.ethBlockNumber().send().getBlockNumber();

        for (BigInteger blockNumber = startBlock; blockNumber.compareTo(nextBlock) < 0; blockNumber = blockNumber.add(BigInteger.ONE)) {
            EthBlock.Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).send().getBlock();

            int transactionsSize = getTransactionsSize(block);
            long timestamp = getBlockTimestamp(block);
            String period = getBlockPeriod(timestamp);

            EthTransactionData build = EthTransactionData.builder()
                    .transactionsSize(transactionsSize)
                    .timestamp(timestamp)
                    .period(period)
                    .build();

            transactionRepository.save(build);
        }
        startBlock = nextBlock;
    }

    @NotNull
    private static String getBlockPeriod(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

        return dateTime.format(formatter);
    }

    private static long getBlockTimestamp(EthBlock.Block block) {
        return block.getTimestamp().longValue() * 1000;
    }

    private static int getTransactionsSize(EthBlock.Block block) {
        return block.getTransactions().size();
    }
}

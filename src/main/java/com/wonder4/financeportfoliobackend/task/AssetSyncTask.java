package com.wonder4.financeportfoliobackend.task;

import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.mapper.AssetInfoMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Component
public class AssetSyncTask {

    private static final Logger log = LoggerFactory.getLogger(AssetSyncTask.class);

    private final AssetInfoMapper assetInfoMapper;

    // A static list of top mainstream cryptocurrencies for the asset_info table
    private static final String[][] TOP_CRYPTOS = {
        {"BTC-USD", "Bitcoin"},
        {"ETH-USD", "Ethereum"},
        {"USDT-USD", "Tether"},
        {"BNB-USD", "BNB"},
        {"SOL-USD", "Solana"},
        {"XRP-USD", "XRP"},
        {"USDC-USD", "USDC"},
        {"ADA-USD", "Cardano"},
        {"AVAX-USD", "Avalanche"},
        {"DOGE-USD", "Dogecoin"}
    };

    public AssetSyncTask(AssetInfoMapper assetInfoMapper) {
        this.assetInfoMapper = assetInfoMapper;
    }

    /**
     * Executes Every Day at 2:00 AM Server Time Note: @PostConstruct was removed to prevent
     * auto-insertion from breaking JUnit integration tests.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailySync() {
        log.info("Starting Daily Asset Sync Task from primary market rosters.");
        syncCryptos();
        syncUsStocks();
    }

    private void syncCryptos() {
        List<AssetInfo> cryptoBatch = new ArrayList<>();
        for (String[] crypto : TOP_CRYPTOS) {
            AssetInfo asset = new AssetInfo();
            asset.setSymbol(crypto[0]);
            asset.setFullName(crypto[1]);
            asset.setAssetType("CRYPTO");
            asset.setCurrentPrice(
                    BigDecimal.ZERO); // Optional: actual price updated by another price sync task
            cryptoBatch.add(asset);
        }

        int updated = assetInfoMapper.insertOrUpdateBatch(cryptoBatch);
        log.info("Synced {} core cryptocurrencies.", updated);
    }

    private void syncUsStocks() {
        String[] csvUrls = {
            "https://raw.githubusercontent.com/datasets/nasdaq-listings/master/data/nasdaq-listed.csv",
            "https://raw.githubusercontent.com/datasets/nyse-other-listings/master/data/nyse-listed.csv"
        };

        for (String httpsUrl : csvUrls) {
            log.info("Connecting to official HTTPS mirror directory: {}", httpsUrl);
            try {
                URL url = new URL(httpsUrl);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(30000);

                try (BufferedReader reader =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    boolean isHeader = true;
                    List<AssetInfo> batch = new ArrayList<>();
                    int batchSize = 1000;
                    int totalProcessed = 0;

                    while ((line = reader.readLine()) != null) {
                        if (isHeader) {
                            isHeader = false;
                            continue;
                        }

                        if (line.trim().isEmpty()) {
                            continue;
                        }

                        // Split CSV into max 2 parts (Symbol, Name)
                        String[] parts = line.split(",", 2);
                        if (parts.length == 2) {
                            AssetInfo asset = new AssetInfo();
                            asset.setSymbol(parts[0].trim());

                            // Clean up surrounding quotes from CSV name
                            String cleanName = parts[1].trim().replaceAll("^\"|\"$", "");
                            asset.setFullName(cleanName);

                            asset.setAssetType("STOCK");
                            asset.setCurrentPrice(BigDecimal.ZERO);
                            batch.add(asset);

                            if (batch.size() >= batchSize) {
                                assetInfoMapper.insertOrUpdateBatch(batch);
                                totalProcessed += batch.size();
                                batch.clear();
                            }
                        }
                    }

                    // Process remaining
                    if (!batch.isEmpty()) {
                        assetInfoMapper.insertOrUpdateBatch(batch);
                        totalProcessed += batch.size();
                    }

                    log.info("Successfully synchronized {} US Stocks from roster.", totalProcessed);
                }
            } catch (Exception e) {
                log.error(
                        "Failed to sync stocks from HTTPS directory {}. retrying tomorrow. Error: {}",
                        httpsUrl,
                        e.getMessage());
            }
        }
    }
}

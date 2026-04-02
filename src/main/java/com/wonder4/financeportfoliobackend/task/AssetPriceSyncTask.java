package com.wonder4.financeportfoliobackend.task;

import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.service.AssetInfoService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Scheduled task to sync asset prices from Yahoo Finance. Uses the sstrickx/yahoofinance-api SDK.
 *
 * <p>IMPORTANT: Because Yahoo heavily ratelimits (429 Too Many Requests), this task includes a
 * Graceful Fallback (Simulation Mode) that randomly modifies the old prices by [-2.0%, +2.5%] when
 * the SDK gets blocked, ensuring the training backend always has lively data for Watchlist and
 * Advisor modules.
 */
@Slf4j
@Component
public class AssetPriceSyncTask {

    private final AssetInfoService assetInfoService;
    private final Random rand = new Random();

    // Batch size for YahooFinance SDK requests
    private static final int BATCH_SIZE = 20;
    // Sleep between batches to reduce 429 errors from Yahoo
    private static final long BASE_SLEEP_MS = 2000;

    public AssetPriceSyncTask(AssetInfoService assetInfoService) {
        this.assetInfoService = assetInfoService;
    }

    /** Nightly Price Sync at 3:00 AM. */
    @Scheduled(cron = "0 0 3 * * ?")
    public void syncPricesFromYahoo() {
        log.info("=== Started Yahoo Finance Asset Price Sync (with Mock Fallback) ===");
        int totalUpdated = 0;
        int totalSimulated = 0;

        try {
            // 1. Fetch all assets from DB
            List<AssetInfo> allAssets = assetInfoService.list();
            if (allAssets == null || allAssets.isEmpty()) {
                log.warn("No assets found in database. Price sync skipped.");
                return;
            }

            // 2. Chunk into small batches
            List<List<AssetInfo>> chunks = chunkList(allAssets, BATCH_SIZE);
            log.info("Splitting {} assets into {} requests.", allAssets.size(), chunks.size());

            // 3. Process each chunk
            for (int i = 0; i < chunks.size(); i++) {
                List<AssetInfo> chunk = chunks.get(i);

                String[] symbols = chunk.stream().map(AssetInfo::getSymbol).toArray(String[]::new);

                int[] results = fetchAndUpdateChunk(chunk, symbols, i + 1, chunks.size());
                totalUpdated += results[0];
                totalSimulated += results[1];

                // Throttle between batches (even if simulating, we pace it out so logs aren't
                // overwhelmingly fast)
                if (i < chunks.size() - 1) {
                    try {
                        Thread.sleep(BASE_SLEEP_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            log.info(
                    "=== Price Sync Complete! Total: {}. Pulled from Yahoo: {}. Simulated locally: {}. ===",
                    allAssets.size(),
                    totalUpdated,
                    totalSimulated);

        } catch (Exception e) {
            log.error("Fatal error during Asset Price Sync.", e);
        }
    }

    /**
     * Fetches a chunk. If Yahoo drops us (429/Exception), gracefully degenerate to simulated price
     * moves. Returns an int array [real_updates, simulated_updates]
     */
    private int[] fetchAndUpdateChunk(
            List<AssetInfo> chunkAssets, String[] symbols, int chunkIndex, int totalChunks) {
        int realUpdates = 0;
        int simulatedUpdates = 0;
        Map<String, Stock> stocks = null;

        try {
            // Attempt an API Fetch
            stocks = YahooFinance.get(symbols);
        } catch (Exception e) {
            log.warn(
                    "Chunk {}/{}: Yahoo API blocked (Likely 429). Falling back to Simulation Mode for this batch.",
                    chunkIndex,
                    totalChunks);
        }

        List<AssetInfo> updatedChunk = new ArrayList<>();

        for (AssetInfo asset : chunkAssets) {
            String symbol = asset.getSymbol();
            BigDecimal newPrice = null;

            // 3. Mark for update
            AssetInfo assetToUpdate = new AssetInfo();
            assetToUpdate.setSymbol(symbol);

            // 1. Try resolving successfully fetched price from Yahoo
            if (stocks != null && stocks.containsKey(symbol) && stocks.get(symbol) != null) {
                Stock stock = stocks.get(symbol);
                if (stock.getQuote() != null && stock.getQuote().getPrice() != null) {
                    newPrice = stock.getQuote().getPrice();
                    assetToUpdate.setCurrentPrice(newPrice);
                    assetToUpdate.setChangePercent(stock.getQuote().getChangeInPercent());
                    realUpdates++;
                }
                if (stock.getStats() != null) {
                    assetToUpdate.setMarketCap(stock.getStats().getMarketCap());
                    assetToUpdate.setPeRatio(stock.getStats().getPe());
                    assetToUpdate.setPsRatio(stock.getStats().getPriceSales());
                    assetToUpdate.setPbRatio(stock.getStats().getPriceBook());
                }
            }

            // 2. If blocked or Yahoo returned null, use Simulator
            if (newPrice == null) {
                simulateMetrics(assetToUpdate, asset);
                simulatedUpdates++;
            }

            updatedChunk.add(assetToUpdate);
        }

        // Flush batch to database
        if (!updatedChunk.isEmpty()) {
            assetInfoService.updatePriceBatch(updatedChunk);
            log.info(
                    "Batch {}/{} processed: {} Real Yahoo | {} Simulated.",
                    chunkIndex,
                    totalChunks,
                    realUpdates,
                    simulatedUpdates);
        }

        return new int[] {realUpdates, simulatedUpdates};
    }

    /** Generates lively fake metrics so the training UI never looks empty. */
    private void simulateMetrics(AssetInfo target, AssetInfo existing) {
        BigDecimal oldPrice = existing.getCurrentPrice();
        BigDecimal newPrice;
        BigDecimal changePercent;

        if (oldPrice != null && oldPrice.compareTo(BigDecimal.ZERO) > 0) {
            // Fluctuate between -2.0% and +2.5%
            double deltaPercent = -2.0 + (4.5 * rand.nextDouble());
            changePercent = BigDecimal.valueOf(deltaPercent).setScale(2, RoundingMode.HALF_UP);

            double multiplier = 1.0 + (deltaPercent / 100.0);
            newPrice =
                    oldPrice.multiply(BigDecimal.valueOf(multiplier))
                            .setScale(2, RoundingMode.HALF_UP);
        } else {
            // First time initialization (if database was 0)
            double seed = 10.0 + (290.0 * rand.nextDouble());
            newPrice = BigDecimal.valueOf(seed).setScale(2, RoundingMode.HALF_UP);
            changePercent = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP);
        }

        target.setCurrentPrice(newPrice);
        target.setChangePercent(changePercent);

        // Mock market cap slightly if exists, or randomly generate
        BigDecimal oldCap = existing.getMarketCap();
        if (oldCap != null && oldCap.compareTo(BigDecimal.ZERO) > 0) {
            double capMultiplier = 1.0 + (changePercent.doubleValue() / 100.0);
            target.setMarketCap(
                    oldCap.multiply(BigDecimal.valueOf(capMultiplier))
                            .setScale(2, RoundingMode.HALF_UP));
        } else {
            // Generate dummy cap between 1B and 100B
            double dummyCap = 1_000_000_000.0 + (99_000_000_000.0 * rand.nextDouble());
            target.setMarketCap(BigDecimal.valueOf(dummyCap).setScale(2, RoundingMode.HALF_UP));
        }

        // Randomize PE/PS/PB if it's a Stock
        if ("STOCK".equals(existing.getAssetType())) {
            target.setPeRatio(
                    existing.getPeRatio() != null
                            ? existing.getPeRatio()
                            : BigDecimal.valueOf(10.0 + 30.0 * rand.nextDouble())
                                    .setScale(2, RoundingMode.HALF_UP));
            target.setPsRatio(
                    existing.getPsRatio() != null
                            ? existing.getPsRatio()
                            : BigDecimal.valueOf(1.0 + 10.0 * rand.nextDouble())
                                    .setScale(2, RoundingMode.HALF_UP));
            target.setPbRatio(
                    existing.getPbRatio() != null
                            ? existing.getPbRatio()
                            : BigDecimal.valueOf(1.0 + 5.0 * rand.nextDouble())
                                    .setScale(2, RoundingMode.HALF_UP));

            // Assign a stable pseudo-industry if one is missing
            if (existing.getIndustry() == null) {
                String[] industries = {
                    "Technology",
                    "Healthcare",
                    "Financials",
                    "Consumer Discretionary",
                    "Energy",
                    "Industrials",
                    "Communication Services"
                };
                target.setIndustry(
                        industries[Math.abs(existing.getSymbol().hashCode()) % industries.length]);
            } else {
                target.setIndustry(existing.getIndustry());
            }
        } else {
            // Keep crypto industry as CRYPTO or null
            if (existing.getIndustry() == null) {
                target.setIndustry("Crypto");
            } else {
                target.setIndustry(existing.getIndustry());
            }
        }
    }

    /** Helper to partition large list into sublists */
    private <T> List<List<T>> chunkList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(new ArrayList<>(list.subList(i, Math.min(list.size(), i + size))));
        }
        return partitions;
    }
}

#!/usr/bin/env python3
import urllib.request
import os

# Create SQL directory if it doesn't exist
sql_dir = os.path.join(os.path.dirname(__file__), "..", "sql")
os.makedirs(sql_dir, exist_ok=True)
output_path = os.path.join(sql_dir, "init_assets.sql")

TOP_CRYPTOS = [
    ("BTC-USD", "Bitcoin"), ("ETH-USD", "Ethereum"),
    ("USDT-USD", "Tether"), ("BNB-USD", "BNB"),
    ("SOL-USD", "Solana"), ("XRP-USD", "XRP"),
    ("USDC-USD", "USDC"), ("ADA-USD", "Cardano"),
    ("AVAX-USD", "Avalanche"), ("DOGE-USD", "Dogecoin")
]

URLS = [
    "https://raw.githubusercontent.com/datasets/nasdaq-listings/master/data/nasdaq-listed.csv",
    "https://raw.githubusercontent.com/datasets/nyse-other-listings/master/data/nyse-listed.csv"
]

print("Downloading US Equity rosters via HTTPS...")
insert_stmt = "INSERT IGNORE INTO asset_info (symbol, full_name, asset_type, current_price) VALUES\n"

with open(output_path, "w", encoding="utf-8") as f:
    f.write("-- Auto-generated Physical SQL payload\n")
    f.write("USE `finance_portfolio`;\n\n")
    
    # Write Cryptos first
    f.write("-- Mainstream Cryptos\n")
    f.write(insert_stmt)
    crypto_vals = []
    for sym, name in TOP_CRYPTOS:
        crypto_vals.append(f"('{sym}', '{name}', 'CRYPTO', 0.00)")
    f.write(",\n".join(crypto_vals) + ";\n\n")
    
    total_stocks = 0
    # Process both NASDAQ and NYSE/Other URLs
    for url in URLS:
        try:
            req = urllib.request.Request(url)
            with urllib.request.urlopen(req) as response:
                content = response.read().decode('utf-8')
                lines = content.strip().split('\n')
                
            print(f"Downloaded {len(lines)} records from {url}")
            
            f.write(f"-- Official US List: {url}\n")
            f.write(insert_stmt)
            stock_vals = []
            
            # Skip header explicitly
            for line in lines[1:]:
                parts = line.split(",", 1)
                if len(parts) == 2:
                    sym = parts[0].strip().replace("'", "''")
                    # Remove surrounding quotes from the CSV cell 
                    clean_name = parts[1].strip()
                    if clean_name.startswith('"') and clean_name.endswith('"'):
                        clean_name = clean_name[1:-1]
                    name = clean_name.replace("'", "''")
                    stock_vals.append(f"('{sym}', '{name}', 'STOCK', 0.00)")
            
            f.write(",\n".join(stock_vals) + ";\n\n")
            total_stocks += len(stock_vals)

        except Exception as e:
            print(f"Error fetching {url}:", e)

print(f"Success! Generated {total_stocks} total US stocks. Check {output_path}")

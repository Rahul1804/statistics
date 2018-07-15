package com.n26.statistics.repositories;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Component;

import com.n26.statistics.model.Transaction;
import static java.time.Instant.now;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Component
public class TransactionRepository {
	
	private Long interval = 60L;
	private ConcurrentNavigableMap<Long, List<Transaction>> transactions = new ConcurrentSkipListMap<>();
	
	public boolean add(Transaction transaction){
		if(Duration.between(transaction.getTime(), now()).getSeconds() <= interval){
			addToTransactions(transaction);
			return true;
		}
		return false;
	}

	private void addToTransactions(Transaction transaction) {
		List<Transaction> transactionsAtSecond = transactions.get(transaction.getTime().getEpochSecond());
		
		if (isNull(transactionsAtSecond)) {
            transactionsAtSecond = new ArrayList<>();
        }
        transactionsAtSecond.add(transaction);
        transactions.put(transaction.getTime().getEpochSecond(), transactionsAtSecond);
	}
	
	public List<Transaction> getTransactions() {
        return transactions.tailMap(getIntervalStartKey()).values().parallelStream().flatMap(Collection::parallelStream).collect(toList());
    }

	private Long getIntervalStartKey() {
		return now().minusSeconds(interval).getEpochSecond();
	}
	
	public void removeOldTransactions(){
		if (transactions.size() > 0) {
			final ConcurrentNavigableMap<Long, List<Transaction>> oldEntries = transactions.headMap(getIntervalStartKey());
			if (oldEntries.size() > 0) {
				oldEntries.clear();
			}
		}
	}
}

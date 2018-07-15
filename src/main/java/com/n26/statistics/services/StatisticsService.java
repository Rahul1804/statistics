package com.n26.statistics.services;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.util.stream.Collectors.summarizingDouble;

import java.util.DoubleSummaryStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.repositories.TransactionRepository;

@Component
public class StatisticsService {

	@Autowired
	private TransactionRepository transactionRepository;

	public boolean addTransaction(Transaction transaction) {
		return transactionRepository.add(transaction);
	}

	public Statistics getStatistics() {
		final DoubleSummaryStatistics stats = transactionRepository.getTransactions().parallelStream()
				.collect(summarizingDouble(Transaction::getAmount));
		return new Statistics(stats.getSum(), getMax(stats), getMin(stats), stats.getCount(), stats.getAverage());
	}

	private double getMax(DoubleSummaryStatistics stats) {
		final double max = stats.getMax();
		return NEGATIVE_INFINITY == max ? 0 : max;
	}

	private double getMin(DoubleSummaryStatistics stats) {
		final double min = stats.getMin();
		return POSITIVE_INFINITY == min ? 0 : min;
	}

}

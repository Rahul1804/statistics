package com.n26.statistics.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.n26.statistics.model.Transaction;
import com.n26.statistics.repositories.TransactionRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class StatisticsServiceTest {
	
	@InjectMocks
	private StatisticsService statisticsService;
	
	@Mock
    private TransactionRepository transactionRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void addTransactionTest() throws Exception {
		final Transaction transaction = new Transaction();
		transaction.setAmount(12.5);
		transaction.setTime(Instant.now());
		
		when(transactionRepository.add(transaction)).thenReturn(true);
		final Boolean isAdded = statisticsService.addTransaction(transaction);
		verify(transactionRepository).add(transaction);
		assertThat(isAdded, is(true));
	}
	

}

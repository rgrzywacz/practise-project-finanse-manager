package summary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import expanse.Expense;
import expanse.ExpenseRepository;
import expanse.PrintExpenseDto;
import income.Income;
import income.IncomeRepository;
import income.PrintIncomeDto;

public class SummaryService {

    private ExpenseRepository expenseRepository;
    private IncomeRepository incomeRepository;

    public SummaryService(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public SummaryDto getSummary(Long accountId) {
        List<Expense> expenses = expenseRepository.findAllByAccountNumber(accountId);
        List<Income> incomes = incomeRepository.findAllByAccountNumber(accountId);
        Set<PrintExpenseDto> expenseDtos = expenses.stream()
                                                   .map(e -> new PrintExpenseDto(e.getId(), e.getAmount().toString() + " zł",e.getComment(), e.getCategory().getName(),
                                                                                 e.getExpanseAddDate().toString(),e.getAccount().getAccountNumber()))
                                                   .collect(Collectors.toSet());
        Set<PrintIncomeDto> incomeDtos = incomes.stream()
                                                .map(i -> new PrintIncomeDto(i.getId(), i.getAmount().toString() + " zł", i.getComment(), i.getIncomeAddDate().toString(), i.getAccount().getAccountNumber()))
                                                .collect(Collectors.toSet());
        SummaryDto summaryDto = new SummaryDto(incomeDtos, expenseDtos);
        return summaryDto;
    }

    public String getBalance() {
        List<Expense> expenses = expenseRepository.findAll();
        List<Income> incomes = incomeRepository.findAll();
        BigDecimal expensesBalance = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomeBalance = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balance = incomeBalance.subtract(expensesBalance);
        return balance.toString();
    }

    public SummaryExtendDtos summaryExtendDtos() {
        List<Object[]> expenseGroupByCategoryList = expenseRepository.findExpenseSummaryGroupByCategory();
        SummaryExtendDtos summaryExtendDtos = new SummaryExtendDtos();
        for (Object[] objects : expenseGroupByCategoryList) {
            BigDecimal totalCost = (BigDecimal) objects[0];
            String categoryName = (String) objects[1];
            Long numberOfExpenses = (Long) objects[2];
            SummaryExpanseDto summaryExpanseDto = new SummaryExpanseDto(categoryName, totalCost, numberOfExpenses);
            summaryExtendDtos.getSummaryExpanseDtoList().add(summaryExpanseDto);
        }

        return summaryExtendDtos;
    }

    public String getBalanceForAccount(long accountId) {
        List<Expense> expenses = expenseRepository.findAllByAccountNumber(accountId);
        List<Income> incomes = incomeRepository.findAllByAccountNumber(accountId);
        BigDecimal expensesBalance = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomeBalance = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balance = incomeBalance.subtract(expensesBalance);
        return balance.toString();
    }

    public SummaryExtendDtos summaryExtendDtosForAccount(long accountId) {
        List<Object[]> expenseGroupByCategoryList = expenseRepository.findExpenseSummaryGroupByCategoryAndAccount(accountId);
        SummaryExtendDtos summaryExtendDtos = new SummaryExtendDtos();
        for (Object[] objects : expenseGroupByCategoryList) {
            BigDecimal totalCost = (BigDecimal) objects[0];
            String categoryName = (String) objects[1];
            Long numberOfExpenses = (Long) objects[2];
            SummaryExpanseDto summaryExpanseDto = new SummaryExpanseDto(categoryName, totalCost, numberOfExpenses);
            summaryExtendDtos.getSummaryExpanseDtoList().add(summaryExpanseDto);
        }

        return summaryExtendDtos;
    }
}

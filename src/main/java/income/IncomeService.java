package income;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import account.Account;
import account.AccountRepository;
import validation.ValidationMessage;

public class IncomeService {

    private final IncomeRepository incomeRepository;

    private final AccountRepository accountRepository;

    public IncomeService(IncomeRepository incomeRepository, AccountRepository accountRepository) {
        this.incomeRepository = incomeRepository;
        this.accountRepository = accountRepository;
    }

    public void addIncome(IncomeDto incomeDto) throws IllegalArgumentException {
        ValidationMessage validationMessage = validateIncomeDto(incomeDto);
        Account byAccountNumber = accountRepository.findByAccountNumber(incomeDto.getAccountNumber());
        if(validationMessage.isValidationResult()) {
            Income income = new Income(incomeDto.getAmount(), incomeDto.getComment(), byAccountNumber);
            incomeRepository.insert(income);
        } else {
            throw new IllegalArgumentException(validationMessage.getMessage());
        }
    }

    public void deleteIncome(@NotNull Long id) {
        Income income = incomeRepository.find(id);
        if (income != null) {
            incomeRepository.delete(income);
        }
    }

    public Set<PrintIncomeDto> getIncomes() {
        List<Income> incomes = incomeRepository.findAll();
        return incomes.stream()
                      .map(i -> new PrintIncomeDto(i.getId(), i.getAmount().toString() + " zł", i.getComment(), i.getIncomeAddDate().toString(), i.getAccount().getAccountNumber()))
                      .collect(Collectors.toSet());
    }

    public ValidationMessage validateIncomeDto(IncomeDto incomeDto) {
        ValidationMessage validationMessage = new ValidationMessage();
        validationMessage.setValidationResult(true);
        if (incomeDto == null || incomeDto.getAmount() == null) {
            validationMessage.setValidationResult(false);
            validationMessage.setMessage(ValidationMessage.MISSING_FIELDS);
        }
        return validationMessage;
    }

    public Set<PrintIncomeDto> getIncomesByAccountId(long accountId) {
        List<Income> incomes = incomeRepository.findAllByAccountNumber(accountId);
        return incomes.stream()
                      .map(i -> new PrintIncomeDto(i.getId(), i.getAmount().toString() + " zł", i.getComment(), i.getIncomeAddDate().toString(), i.getAccount().getAccountNumber()))
                      .collect(Collectors.toSet());
    }
}

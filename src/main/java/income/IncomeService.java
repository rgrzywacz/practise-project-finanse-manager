package income;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import account.Account;
import account.AccountDao;
import validation.ValidationMessage;

public class IncomeService {

    private final IncomeDao incomeDao;

    private final AccountDao accountDao;

    public IncomeService(IncomeDao incomeDao, AccountDao accountDao) {
        this.incomeDao = incomeDao;
        this.accountDao = accountDao;
    }

    public void addIncome(IncomeDto incomeDto) throws IllegalArgumentException {
        ValidationMessage validationMessage = validateIncomeDto(incomeDto);
        Account byAccountNumber = accountDao.findByAccountNumber(incomeDto.getAccountNumber());
        if(validationMessage.isValidationResult()) {
            Income income = new Income(incomeDto.getAmount(), incomeDto.getComment(), byAccountNumber);
            incomeDao.insert(income);
        } else {
            throw new IllegalArgumentException(validationMessage.getMessage());
        }
    }

    public void deleteIncome(@NotNull Long id) {
        Income income = incomeDao.find(id);
        if (income != null) {
            incomeDao.delete(income);
        }
    }

    public Set<PrintIncomeDto> getIncomes() {
        List<Income> incomes = incomeDao.findAll();
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
        List<Income> incomes = incomeDao.findAllByAccountNumber(accountId);
        return incomes.stream()
                      .map(i -> new PrintIncomeDto(i.getId(), i.getAmount().toString() + " zł", i.getComment(), i.getIncomeAddDate().toString(), i.getAccount().getAccountNumber()))
                      .collect(Collectors.toSet());
    }
}

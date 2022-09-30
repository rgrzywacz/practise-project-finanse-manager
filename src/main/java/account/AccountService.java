package account;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import expanse.Expense;
import validation.ValidationMessage;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void addAccount(String accountNumber, String name) {
        ValidationMessage validationMessage = addAccountValidator(accountNumber);
        if (validationMessage.isValidationResult()) {
            Account account = new Account(accountNumber, name);
            accountRepository.insertAccount(account);
        } else {
            throw new IllegalArgumentException(validationMessage.getMessage());
        }
    }

    public List<AccountDto> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(a -> new AccountDto(a.getId(), a.getAccountNumber(), a.getName())).collect(Collectors.toList());
    }

    private ValidationMessage addAccountValidator(String accountNumber) {
        ValidationMessage validationMessage = new ValidationMessage(ValidationMessage.OK, true);
        if (accountNumber.length() != 26) {
            validationMessage.setMessage(ValidationMessage.WRONG_ACCOUNT_NUMBER);
            validationMessage.setValidationResult(false);
        }
        return validationMessage;
    }

    private ValidationMessage deleteAccountValidator(long accountId) {
        Account account = accountRepository.findAccount(accountId);
        Set<Expense> expenses = account.getExpenses();
        ValidationMessage validationMessage = new ValidationMessage(ValidationMessage.OK, true);
        if (expenses.size() > 0) {
            validationMessage.setMessage(ValidationMessage.ACCOUNT_ASSOCIATED_WITH_EXPENSES);
            validationMessage.setValidationResult(false);
        }
        return validationMessage;
    }

    public void deleteAccount(long accountId) {
        ValidationMessage validationMessage = deleteAccountValidator(accountId);
        if (validationMessage.isValidationResult()) {
            accountRepository.deleteAccount(accountId);
        } else {
            throw new IllegalArgumentException(validationMessage.getMessage());
        }
    }
}

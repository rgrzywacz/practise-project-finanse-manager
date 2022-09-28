package validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationMessage {

    public static final String MISSING_FIELDS = "ERROR: New entry has not been added. Some fields are missing.";
    public static final String MISSING_CATEGORY = "ERROR: Category not exists.";
    public static final String AMOUNT_LOWER_THAN_0 = "ERROR: Amount must be greater than 0.";
    public static final String CATEGORY_ASSOCIATED_WITH_ENTRIES = "ERROR: Category is associated with other expenses, please delete linking expenses before delete category.";
    public static final String WRONG_ACCOUNT_NUMBER= "ERROR: Account number must be 26 numbers.";
    public static final String ACCOUNT_NOT_EXISTS= "ERROR: Account not exists.";

    public static final String ACCOUNT_ASSOCIATED_WITH_EXPENSES = "ERROR: Account is associated with expenses, please delete expenses associated with account before delete account.";
    public static final String OK = "OK";

    private String message;
    private boolean validationResult;
}

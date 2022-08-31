package validation;

import lombok.Data;

@Data
public class ValidationMessage {

    public static final String MISSING_FIELDS = "Nowy rekod nie został dodany. Nie uzupełniono wszystkich pól ";
    public static final String OK = "OK";

    private String message;
    private boolean validationResult;
}

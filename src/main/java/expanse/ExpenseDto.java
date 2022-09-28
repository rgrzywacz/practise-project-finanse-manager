package expanse;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseDto {
    private Long id;
    private BigDecimal amount;
    private String comment;
    private String category;

    private String accountNumber;

    public ExpenseDto(BigDecimal amount, String comment, String category, String accountNumber) {
        this.amount = amount;
        this.comment = comment;
        this.category = category;
        this.accountNumber = accountNumber;
    }
}

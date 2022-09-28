package expanse;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrintExpenseDto {
    private Long id;
    private String amount;
    private String comment;
    private String category;
    private String expanseAddDate;
    private String accountNumber;
}

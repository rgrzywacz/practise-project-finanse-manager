package income;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String comment;

    @Column(name = "income_add_date")
    private LocalDate incomeAddDate;

    public Income(BigDecimal amount, String comment) {
        this.amount = amount;
        this.comment = comment;
        this.incomeAddDate = LocalDate.now();
    }
}

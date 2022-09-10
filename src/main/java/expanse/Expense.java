package expanse;

import java.math.BigDecimal;
import java.time.LocalDate;

import category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String comment;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "expanse_add_date")
    private LocalDate expanseAddDate;

    public Expense(BigDecimal amount, String comment, Category category) {
        this.amount = amount;
        this.comment = comment;
        this.category = category;
        this.expanseAddDate = LocalDate.now();
    }
}

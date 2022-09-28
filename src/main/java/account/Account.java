package account;

import java.util.Set;

import expanse.Expense;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    private String name;

    @OneToMany(mappedBy = "account")
    private Set<Expense> expenses;

    public Account(String accountNumber, String name) {
        this.accountNumber = accountNumber;
        this.name = name;
    }
}

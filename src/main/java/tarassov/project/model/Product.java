package tarassov.project.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @Size(min = 5, max = 20)
    private String serialNumber;

    @OneToMany
    private List<Picture> picture;

    @NotNull
    @Size(min = 5, max = 50)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @NotNull
    private Double price;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date purchaseDate;

    @ManyToMany
    private List<Storage> storages;
}
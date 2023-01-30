package traveler.bookclub.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "review")
public class Review {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;
}

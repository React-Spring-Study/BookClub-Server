package traveler.bookclub.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "group_id")
    private Long id;
}

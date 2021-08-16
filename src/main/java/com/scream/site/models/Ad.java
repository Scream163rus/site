package com.scream.site.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "AD",schema = "public")
public class Ad {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long Id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    @Column(name = "filename")
    private String filename;
    @Column(name = "description")
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Column(name = "title")
    @NotBlank(message = "Title is mandatory")
    private String title;
    @Column(name = "category")
    @NotBlank(message = "Category is mandatory")
    private String category;
    @Column(name = "numberPhone")
    @NotBlank(message = "NumberPhone is mandatory")
    @Pattern(regexp="^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$")
    private String numberPhone;
    @Column(name="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDate date;
    public String getAuthorName(){
        return author != null ? author.getUsername() : "<none>";
    }
}

package pl.library.components.author;


import javax.validation.constraints.NotEmpty;

public class AuthorDto {

    private Long authorId;
    @NotEmpty(message = "{pl.library.components.author.Author.authorName.NotEmpty}")
    private String authorName;
    @NotEmpty(message = "{pl.library.components.author.Author.authorSurname.NotEmpty}")
    private String authorSurname;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

}

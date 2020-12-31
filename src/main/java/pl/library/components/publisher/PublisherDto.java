package pl.library.components.publisher;

import javax.validation.constraints.NotEmpty;

public class PublisherDto {

    private Long publisherId;
    @NotEmpty(message = "{pl.library.components.publisher.Publisher.publisherName.NotEmpty}")
    private String publisherName;

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}

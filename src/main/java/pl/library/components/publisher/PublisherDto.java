package pl.library.components.publisher;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublisherDto that = (PublisherDto) o;
        return Objects.equals(publisherId, that.publisherId) &&
                Objects.equals(publisherName, that.publisherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherId, publisherName);
    }
}

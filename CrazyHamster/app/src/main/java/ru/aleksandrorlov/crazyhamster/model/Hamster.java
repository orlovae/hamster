package ru.aleksandrorlov.crazyhamster.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by alex on 19.05.17.
 */

public class Hamster {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String imageURL;

    public Hamster() {
    }

    public Hamster(String title, String description, String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(description).append(imageURL).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Hamster) == false) {
            return false;
        }
        Hamster rhs = ((Hamster) other);
        return new EqualsBuilder().append(title, rhs.title).append(description, rhs.description).
                append(imageURL, rhs.imageURL).isEquals();
    }

}

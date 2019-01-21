package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Property {

    @Id
    private String id;

    private String value;

    public Property() {
    }

    public Property(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && id.equals(((Property) obj).id);
    }

    @Override
    public String toString() {
        return "Property{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

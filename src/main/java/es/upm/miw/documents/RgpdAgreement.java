package es.upm.miw.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;


@Document
public class RgpdAgreement {

    @Id
    private String id;

    private RgpdAgreementType type;

    private byte[] agreement;

    @DBRef
    private User assignee;

    public RgpdAgreement() {
        this(RgpdAgreementType.BASIC, null, null);
    }

    public RgpdAgreement(RgpdAgreementType type, byte[] agreement, User assignee) {
        this.type = type;
        this.agreement = agreement;
        this.assignee = assignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RgpdAgreementType getType() {
        return type;
    }

    public void setType(RgpdAgreementType type) {
        this.type = type;
    }

    public byte[] getAgreement() {
        return agreement;
    }

    public void setAgreement(byte[] agreement) {
        this.agreement = agreement;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && id.equals(((RgpdAgreement) obj).id);
    }

    @Override
    public String toString() {
        return "RgpdAgreement{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", agreement=" + Arrays.toString(agreement) +
                ", assignee=" + assignee +
                '}';
    }
}

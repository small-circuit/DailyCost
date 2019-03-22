package virgil.dailycost.models;

import android.support.annotation.Nullable;

/**
 * Created by VIRGILH on 4/30/2018.
 */

public class SubjectSpend {

    private Integer id;
    private String subject;
    private Float spend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Float getSpend() {
        return spend;
    }

    public void setSpend(Float spend) {
        this.spend = spend;
    }

    public SubjectSpend(Integer id, String subject, Float spend) {
        this.id = id;
        this.subject = subject;
        this.spend = spend;
    }
}

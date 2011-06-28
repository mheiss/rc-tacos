package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "FilterRule")
public class FilterRule extends EntityImpl {

    private static final long serialVersionUID = 7446082387075500566L;

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 255)
    private String name;

    @Column(length = 1024)
    private String description;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<FilterRuleParam> params;

    // ---------------------------------
    // EntityImpl
    // ---------------------------------
    @Override
    public Object getOid() {
        return id;
    }

    @Override
    public String getDisplayString() {
        return name;
    }

    // ---------------------------------
    // Object related methods
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        FilterRule rhs = (FilterRule) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // ---------------------------------
    // Business relevant methods
    // ---------------------------------
    public FilterRuleParam getParam(String id) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        for (FilterRuleParam param : params) {
            if (param.getName().equals(id)) {
                return param;
            }
        }
        return null;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParams(List<FilterRuleParam> params) {
        this.params = params;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<FilterRuleParam> getParams() {
        if (params == null) {
            params = new ArrayList<FilterRuleParam>();
        }
        return params;
    }

}

package io.qiot.covid19.edge.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StationDataBean {

    public String id;
    public String serial;
    public String name;
    public CoordinatesBean coordinates;
    public String tspass;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StationDataBean other = (StationDataBean) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StationDataBean [id=");
        builder.append(id);
        builder.append(", serial=");
        builder.append(serial);
        builder.append(", name=");
        builder.append(name);
        builder.append(", coordinates=");
        builder.append(coordinates);
        builder.append(", tspass=");
        builder.append(tspass);
        builder.append("]");
        return builder.toString();
    }

}

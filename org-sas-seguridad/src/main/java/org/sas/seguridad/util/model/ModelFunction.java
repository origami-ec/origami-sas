package org.sas.seguridad.util.model;

import java.util.Objects;

public class ModelFunction {

    private String funtion;
    private String field;
    private String returnClass;
    private Object result;

    public ModelFunction() {
        funtion = "SUM";
    }

    public ModelFunction(String field) {
        funtion = "SUM";
        this.field = field;
    }

    public ModelFunction(String funtion, String field) {
        this.funtion = funtion;
        this.field = field;
    }

    public ModelFunction(String funtion, String field, String returnClass) {
        this.funtion = funtion;
        this.field = field;
        this.returnClass = returnClass;
    }

    public String getFuntion() {
        return funtion;
    }

    public void setFuntion(String funtion) {
        this.funtion = funtion;
    }

    public String getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.funtion);
        hash = 17 * hash + Objects.hashCode(this.returnClass);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelFunction other = (ModelFunction) obj;
        if (!Objects.equals(this.funtion, other.funtion)) {
            return false;
        }
        return Objects.equals(this.returnClass, other.returnClass);
    }

    @Override
    public String toString() {
        return "ModelFunction{" + "funtion=" + funtion + ", field=" + field + ", returnClass=" + returnClass + ", result=" + result + '}';
    }
}

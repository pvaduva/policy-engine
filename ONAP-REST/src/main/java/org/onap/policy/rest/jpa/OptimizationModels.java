/*-
 * ============LICENSE_START=======================================================
 * ONAP-REST
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.policy.rest.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
/*
 * JPA for the OOF Models. 
 * 
 * @version: 0.1
 */


@Entity
@Table(name="OptimizationModels")
@NamedQuery(name="OptimizationModels.findAll", query="SELECT b FROM OptimizationModels b ")
public class OptimizationModels implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(name="modelName", nullable=false, unique=true)
    @OrderBy("asc")
    private String modelName;

    @Column(name="description", nullable=true, length=2048)
    private String description;

    @Column(name="dependency", nullable=true, length=2048)
    private String dependency;

    @Column(name="attributes", nullable=false, length=255)
    private String attributes;

    @Column(name="ref_attributes", nullable=false, length=255)
    private String refattributes;

    @Column (name="sub_attributes", nullable=false, length=2000)
    private String subattributes;

    @Column (name="dataOrderInfo", nullable=true, length=2000)
    private String dataOrderInfo;

    @Column (name="version", nullable=false, length=2000)
    private String version;

    @Column (name="enumValues", nullable=false, length=2000)
    private String enumValues;

    @Column (name="annotation", nullable=false, length=2000)
    private String annotation;

    public String getSubattributes() {
        return subattributes;
    }

    public void setSubattributes(String subattributes) {
        this.subattributes = subattributes;
    }

    public String getDataOrderInfo() {
        return dataOrderInfo;
    }

    public void setDataOrderInfo(String dataOrderInfo) {
        this.dataOrderInfo = dataOrderInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @ManyToOne
    @JoinColumn(name="imported_by")
    private UserInfo userCreatedBy;

    public UserInfo getUserCreatedBy() {
        return userCreatedBy;
    }

    public void setUserCreatedBy(UserInfo userCreatedBy) {
        this.userCreatedBy = userCreatedBy;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getRefattributes() {
        return refattributes;
    }

    public void setRefattributes(String refattributes) {
        this.refattributes = refattributes;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getModelName(){
        return this.modelName;
    }

    public void setModelName(String modelName){
        this.modelName = modelName;
    }

    public String getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(String enumValues) {
        this.enumValues = enumValues;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}

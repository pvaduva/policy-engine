/*-
 * ============LICENSE_START=======================================================
 * ONAP-REST
 * ================================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
/*
 */
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="PrefixList")
@NamedQuery(name="PrefixList.findAll", query="SELECT e FROM PrefixList e ")
public class PrefixList implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(name="pl_name", nullable=false)
    @OrderBy("asc")
    private String prefixListName;

    @Column(name="description", nullable=false)
    private String description;

    @Column(name="pl_value", nullable=false)
    private String prefixListValue;

    public String getPrefixListName() {
        return this.prefixListName;
    }

    public void setPrefixListName(String prefixListName) {
        this.prefixListName = prefixListName;

    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;

    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPrefixListValue() {
        return this.prefixListValue;
    }

    public void setPrefixListValue(String prefixListValue) {
        this.prefixListValue = prefixListValue;
    }

}

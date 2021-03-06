package com.sequenceiq.cloudbreak.api.model;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sequenceiq.cloudbreak.doc.ModelDescriptions.StackModelDescription;
import com.sequenceiq.cloudbreak.validation.ValidKerberos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@ValidKerberos
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class KerberosRequest extends KerberosBase {

    @ApiModelProperty(StackModelDescription.KERBEROS_MASTER_KEY)
    @Size(max = 50, min = 3, message = "The length of the Kerberos password has to be in range of 3 to 50")
    private String masterKey;

    @ApiModelProperty(StackModelDescription.KERBEROS_PASSWORD)
    @Size(max = 50, min = 5, message = "The length of the Kerberos password has to be in range of 5 to 50")
    private String password;

    @ApiModelProperty(StackModelDescription.KERBEROS_PRINCIPAL)
    private String principal;

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}

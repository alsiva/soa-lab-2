/*
 * SOA LAB 1
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.ifmo.soalab2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * ProductOrganization
 */
@XmlRootElement(name = "ProductOrganization")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOrganization   {

  private Integer orgId = null;


  private String name = null;


  private String fullName = null;


  private Float annualTurnover = null;

  /**
   * Gets or Sets orgType
   */
  public enum OrgTypeEnum {
    COMMERCIAL("COMMERCIAL"),
    
    GOVERNMENT("GOVERNMENT"),
    
    TRUST("TRUST"),
    
    PRIVATE_LIMITED_COMPANY("PRIVATE_LIMITED_COMPANY"),
    
    OPEN_JOINT_STOCK_COMPANY("OPEN_JOINT_STOCK_COMPANY");

    private String value;

    OrgTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }


    public static OrgTypeEnum fromValue(String text) {
      for (OrgTypeEnum b : OrgTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  private OrgTypeEnum orgType = null;


  private ProductOrganizationPostalAddress postalAddress = null;

  public ProductOrganization orgId(Integer orgId) {
    this.orgId = orgId;
    return this;
  }

  /**
   * Get orgId
   * minimum: 1
   * @return orgId
   **/
  @Schema(example = "3", description = "")
 @Min(1)  public Integer getOrgId() {
    return orgId;
  }

  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }

  public ProductOrganization name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
  @Schema(example = "Егор", description = "")
 @Size(min=1)  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductOrganization fullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   * @return fullName
   **/
  @Schema(example = "Егор Кривоносов Дмитриевич", description = "")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public ProductOrganization annualTurnover(Float annualTurnover) {
    this.annualTurnover = annualTurnover;
    return this;
  }

  /**
   * Get annualTurnover
   * minimum: 1
   * @return annualTurnover
   **/
  @Schema(description = "")
 @DecimalMin("1")  public Float getAnnualTurnover() {
    return annualTurnover;
  }

  public void setAnnualTurnover(Float annualTurnover) {
    this.annualTurnover = annualTurnover;
  }

  public ProductOrganization orgType(OrgTypeEnum orgType) {
    this.orgType = orgType;
    return this;
  }

  /**
   * Get orgType
   * @return orgType
   **/
  @Schema(example = "COMMERCIAL", description = "")
  public OrgTypeEnum getOrgType() {
    return orgType;
  }

  public void setOrgType(OrgTypeEnum orgType) {
    this.orgType = orgType;
  }

  public ProductOrganization postalAddress(ProductOrganizationPostalAddress postalAddress) {
    this.postalAddress = postalAddress;
    return this;
  }

  /**
   * Get postalAddress
   * @return postalAddress
   **/
  @Schema(description = "")
  @Valid
  public ProductOrganizationPostalAddress getPostalAddress() {
    return postalAddress;
  }

  public void setPostalAddress(ProductOrganizationPostalAddress postalAddress) {
    this.postalAddress = postalAddress;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductOrganization productOrganization = (ProductOrganization) o;
    return Objects.equals(this.orgId, productOrganization.orgId) &&
        Objects.equals(this.name, productOrganization.name) &&
        Objects.equals(this.fullName, productOrganization.fullName) &&
        Objects.equals(this.annualTurnover, productOrganization.annualTurnover) &&
        Objects.equals(this.orgType, productOrganization.orgType) &&
        Objects.equals(this.postalAddress, productOrganization.postalAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orgId, name, fullName, annualTurnover, orgType, postalAddress);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOrganization {\n");
    
    sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    annualTurnover: ").append(toIndentedString(annualTurnover)).append("\n");
    sb.append("    orgType: ").append(toIndentedString(orgType)).append("\n");
    sb.append("    postalAddress: ").append(toIndentedString(postalAddress)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

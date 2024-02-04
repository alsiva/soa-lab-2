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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.ifmo.soalab2.DateAdapter;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Product
 */
@XmlRootElement(name = "Product")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "products")
public class Product implements OneOfProductsItems, Serializable {

    @Id
    @GeneratedValue
    private Integer id;


    private String name;

    @Embedded
    private ProductCoordinates coordinates;

    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date creationDate;
    private Float price;
    private Long manufactureCost;

    @Column(name = "unitOfMeasure")
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne
    private ProductOrganization organization;

    //Не удалять необходим для работы JAXB
    public Product() {
    }

    public Product(String name, ProductCoordinates coordinates, Date creationDate, Float price, Long manufactureCost, UnitOfMeasure unitOfMeasure, ProductOrganization organization) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.manufactureCost = manufactureCost;
        this.unitOfMeasure = unitOfMeasure;
        this.organization = organization;
    }


    /**
     * Get id
     *
     * @return id
     **/

    @Schema(example = "1", description = "")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Get name
     *
     * @return name
     **/

    @Schema(example = "Ice cream", description = "")
    @Size(min = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get coordinates
     *
     * @return coordinates
     **/
    @Schema(description = "")
    @Valid
    public ProductCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ProductCoordinates coordinates) {
        this.coordinates = coordinates;
    }


    /**
     * Get creationDate
     *
     * @return creationDate
     **/

    @Schema(description = "")
    @Valid
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Get price
     * minimum: 1
     *
     * @return price
     **/

    @Schema(description = "")
    @DecimalMin("1")
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Product manufactureCost(Long manufactureCost) {
        this.manufactureCost = manufactureCost;
        return this;
    }

    /**
     * Get manufactureCost
     *
     * @return manufactureCost
     **/

    @Schema(example = "300", description = "")
    public Long getManufactureCost() {
        return manufactureCost;
    }

    public void setManufactureCost(Long manufactureCost) {
        this.manufactureCost = manufactureCost;
    }

    /**
     * Get unitOfMeasure
     *
     * @return unitOfMeasure
     **/
    @Schema(example = "METERS", description = "")
    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    /**
     * Get organization
     *
     * @return organization
     **/

    @Schema(description = "")
    @Valid
    public ProductOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(ProductOrganization organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(this.id, product.id) &&
                Objects.equals(this.name, product.name) &&
                Objects.equals(this.coordinates, product.coordinates) &&
                Objects.equals(this.creationDate, product.creationDate) &&
                Objects.equals(this.price, product.price) &&
                Objects.equals(this.manufactureCost, product.manufactureCost) &&
                Objects.equals(this.unitOfMeasure, product.unitOfMeasure) &&
                Objects.equals(this.organization, product.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, manufactureCost, unitOfMeasure, organization);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Product {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
        sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    manufactureCost: ").append(toIndentedString(manufactureCost)).append("\n");
        sb.append("    unitOfMeasure: ").append(toIndentedString(unitOfMeasure)).append("\n");
        sb.append("    organization: ").append(toIndentedString(organization)).append("\n");
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

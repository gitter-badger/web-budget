/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.entities.registration;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.infrastructure.utils.RandomCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;

/**
 * The representation of a contact in the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.2.0, 07/04/2015
 */
@Entity
@Audited
@Table(name = "contacts")
@AuditTable(value = "audit_contacts")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Contact extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 6, unique = true)
    private final String code;
    
    @Getter
    @Setter
    @NotBlank(message = "{contact.name}")
    @Column(name = "name", nullable = false, length = 90)
    private String name;
    @Setter
    @Getter
    @Column(name = "document", length = 25)
    private String document;
    @Setter
    @Getter
    @Column(name = "birth_date")
    @NotNull(message = "{contact.birth-date}")
    private LocalDate birthDate;
    @Setter
    @Getter
    @Column(name = "other_informations", columnDefinition = "TEXT")
    private String otherInformations;

    @Setter
    @Getter
    @Column(name = "zipcode", length = 9)
    private String zipcode;
    @Setter
    @Getter
    @Column(name = "street", length = 90)
    private String street;
    @Setter
    @Getter
    @Column(name = "number", length = 8)
    private String number;
    @Setter
    @Getter
    @Column(name = "complement", length = 45)
    private String complement;
    @Setter
    @Getter
    @Column(name = "neighborhood", length = 45)
    private String neighborhood;
    @Setter
    @Getter
    @Column(name = "province", length = 45)
    @NotBlank(message = "{contact.province}")
    private String province;
    @Setter
    @Getter
    @Column(name = "city", length = 45)
    @NotBlank(message = "{contact.city}")
    private String city;
    @Setter
    @Getter
    @Column(name = "email", length = 90)
    private String email;
    
    @Getter
    @Setter
    @Column(name = "blocked", nullable = false)
    private boolean blocked;
    
    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{contact.contact-type}")
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    /**
     * Fetch in a subselect because every time we request a contact the numbers 
     * needs to be in the object
     */
    @Setter
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "contact", fetch = EAGER, cascade = REMOVE)
    private List<Telephone> telephones;
    
    @Getter
    @Transient
    private final List<Telephone> deletedTelephones;
    
    /**
     * Default constructor
     */
    public Contact() {
        this.code = RandomCode.alphanumeric(6);
        
        this.telephones = new ArrayList<>();
        this.deletedTelephones = new ArrayList<>();
    }

    /**
     * Get the {@link Telephone} of the contact
     * 
     * @return a {@link List} of {@link Telephone} for this contact
     */
    public List<Telephone> getTelephones() {
        return Collections.unmodifiableList(this.telephones);
    }
    
    /**
     * Format and return the document of this contact
     *
     * @return the contact document formatted
     */
    public String getDocumentFormated() {
        return this.contactType.formatDocument(this.document);
    }
    
    /**
     * Remove all the {@link Telephone} of this contact
     */
    public void clearTelephones() {
        this.telephones.clear();
    }
    
    /**
     * Validate the contact document by the document type defined by the {@link ContactType} enum
     */
    public void validateDocument() {
        this.contactType.validateDocument(this.document.replace("\\w", ""));
    }
    
    /**
     * Add a new {@link Telephone} to this contact
     * 
     * @param telephone the {@link Telephone} to add
     */
    public void addTelephone(Telephone telephone) {
        this.telephones.add(telephone);
    }

    /**
     * Remove a {@link Telephone} of this contact
     *
     * @param telephone the {@link Telephone} to remove
     */
    public void removeTelephone(Telephone telephone) {
        this.telephones.remove(telephone);
        this.addDeletedTelephone(telephone);
    }

    /**
     * Add a already persisted {@link Telephone} to the deletion list
     * 
     * @param telephone the {@link Telephone} to be add to the deletion list
     */
    private void addDeletedTelephone(Telephone telephone) {
        if (telephone.isSaved()) {
            this.deletedTelephones.add(telephone);
        }
    }

    /**
     * For a given {@link Address} this method set all the values in the correct fields of the contact
     * 
     * @param address the {@link Address} to be set to this contact
     */
    public void setAddress(Address address) {
        this.zipcode = address.getZipcode();
        this.street = address.getStreet();
        this.neighborhood = address.getNeighborhood();
        this.complement = address.getComplement();
        this.city = address.getCity();
        this.province = address.getProvince();
    }
}

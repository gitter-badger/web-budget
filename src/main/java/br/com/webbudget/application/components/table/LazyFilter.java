/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.components.table;

import lombok.Getter;
import lombok.Setter;

/**
 * This filter implementation is a helper class to help the lazy loading feature
 * of the datatables 
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/03/2018
 */
public final class LazyFilter {

    @Getter
    @Setter
    public String value;
    @Getter
    @Setter
    public EntityStatus entityStatus;

    /**
     * Private constructor to prevent misuse 
     */
    private LazyFilter() {
       this.entityStatus = EntityStatus.UNBLOCKED; 
    }
    
    /**
     * This replace the default construtor to build istances of this filter
     * 
     * @return a instance of this filter
     */
    public static LazyFilter getInstance() {
        return new LazyFilter();
    }

    /**
     * Clear the filter internal parameters
     */
    public void clear() {
        this.value = null;
        this.entityStatus = EntityStatus.UNBLOCKED;
    }
    
    /**
     * The status value, if the entity to be queried is blocked, unblocked or 
     * if all entities will returned
     * 
     * @return the status value
     */
    public Boolean getEntityStatusValue() {
        return entityStatus.value();
    }
    
    /**
     * @return the values to be used on the selection box of the status
     */
    public EntityStatus[] getEntityStatusValues() {
        return EntityStatus.values();
    }
    
    /**
     * The enum representation of the possible entity status
     */
    public enum EntityStatus {

        ALL("entity-status.all", null),
        BLOCKED("entity-status.blocked", Boolean.TRUE),
        UNBLOCKED("entity-status.unblocked", Boolean.FALSE);

        private final Boolean value;
        private final String description;

        /**
         * Constructor...
         *
         * @param description the i18n description
         * @param value the value
         */
        private EntityStatus(String description, Boolean value) {
            this.value = value;
            this.description = description;
        }

        /**
         * {@inheritDoc }
         * 
         * @return
         */
        @Override
        public String toString() {
            return this.description;
        }

        /**
         * @return the value of the current enum instance
         */
        public Boolean value() {
            return this.value;
        }
    }
}